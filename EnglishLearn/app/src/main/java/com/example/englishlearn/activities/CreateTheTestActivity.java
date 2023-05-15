package com.example.englishlearn.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.englishlearn.R;
import com.example.englishlearn.api.APIConst;
import com.example.englishlearn.api.APIServer;
import com.example.englishlearn.handlerothers.ConstSendData;
import com.example.englishlearn.handlerothers.RealPathUtil;
import com.example.englishlearn.models.TheTestLocal;
import com.example.englishlearn.sharedpreferences.DataLocalManage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTheTestActivity extends AppCompatActivity {
    private EditText txtid, txtname, txtnumber;
    private Button btnCreate;
    private ImageView image;
    private ProgressDialog mDialog;
    private Uri mUri = null;
    private final int reqiest_code_permission = 10000;
    private TheTestLocal mTheTestLocal;

    private ActivityResultLauncher launch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        mUri = uri;
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
                            image.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_the_test);

        mappingElements();
        eventElements();
    }

    private void mappingElements() {
        mDialog = new ProgressDialog(this);
        mDialog.setTitle(R.string.title_progess);
        mDialog.setMessage(getResources().getString(R.string.content_progess));
        mDialog.setCancelable(false);
        txtid = findViewById(R.id.actt_id);
        txtname = findViewById(R.id.actt_name);
        txtnumber = findViewById(R.id.actt_number);
        image = findViewById(R.id.actt_img);
        btnCreate = findViewById(R.id.actt_btn);
    }

    private void eventElements() {
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerCreateTheTest();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckPermissionMedia();
            }
        });
    }

    private void CheckPermissionMedia() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            openAndGetImage();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, reqiest_code_permission);
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                openAndGetImage();
            }
        }
    }

    private void openAndGetImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        launch.launch(intent);
    }

    private void handlerCreateTheTest() {
        if (!validatorEditText(txtid) ||
                !validatorEditText(txtname) ||
                !validatorEditText(txtnumber)) {
            return;
        }

        String id = txtid.getText().toString();
        String name = txtname.getText().toString();
        String number = txtnumber.getText().toString();
        String username = DataLocalManage.getUsertoCheck();
        String date = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());

        int minValues = Integer.parseInt(number);

        if (minValues < 10) {
            txtnumber.setText("");
            validatorEditText(txtnumber);
            Toast.makeText(this, getString(R.string.min_number), Toast.LENGTH_LONG).show();
            return;
        }
        mTheTestLocal = new TheTestLocal(id, name, Integer.parseInt(number));

        if (mUri != null) {
            RequestBody idInsert = RequestBody.create(MediaType.parse("multipart/form-data"), id);
            RequestBody nameInsert = RequestBody.create(MediaType.parse("multipart/form-data"), name);
            RequestBody dateInsert = RequestBody.create(MediaType.parse("multipart/form-data"), date);
            RequestBody usernameInsert = RequestBody.create(MediaType.parse("multipart/form-data"), username);

            String fileString = RealPathUtil.getRealPath(this, mUri);
            File file = new File(fileString);
            RequestBody avatar = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part avartupload = MultipartBody.Part.createFormData(APIConst.AVATAR, file.getName(), avatar);
            callAPIupdateAvatar(idInsert, nameInsert, usernameInsert, dateInsert, avartupload);
        } else {
            callAPIupdateAvatar(id, name, username, date);
        }
    }

    private void callAPIupdateAvatar(RequestBody idInsert,
                                     RequestBody nameInsert,
                                     RequestBody usernameInsert,
                                     RequestBody dateInsert,
                                     MultipartBody.Part avartupload) {
        mDialog.show();
        APIServer.api.insertDataInTheTest(idInsert, nameInsert,
                usernameInsert, dateInsert,
                avartupload).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equalsIgnoreCase("success")) {
                    if (mTheTestLocal != null) {
                        toActivity(mTheTestLocal);
                    }
                    mDialog.dismiss();
                } else {
                    resetValuesandnNotification();
                    mDialog.dismiss();
                    return;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(CreateTheTestActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void callAPIupdateAvatar(String id,
                                     String name,
                                     String user,
                                     String date) {
        mDialog.show();
        APIServer.api.insertDataInTheTest(id, name,
                user, date).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equalsIgnoreCase("success")) {
                    if (mTheTestLocal != null) {
                        toActivity(mTheTestLocal);
                    }
                    mDialog.dismiss();
                } else {
                    resetValuesandnNotification();
                    mDialog.dismiss();
                    return;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(CreateTheTestActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void toActivity(TheTestLocal item) {
        Intent intent = new Intent(this, CreateQuestionOfTestActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstSendData.KEY_SEND_OBJECT_TEST_FRAGMENT, item);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void resetValuesandnNotification() {
        txtid.setText("");
        txtid.setHintTextColor(getResources().getColor(R.color.black));
        txtid.requestFocus();

        txtname.setText("");
        txtname.setHintTextColor(getResources().getColor(R.color.black));
        txtname.requestFocus();

        txtnumber.setText("");
        txtnumber.setHintTextColor(getResources().getColor(R.color.black));
        txtnumber.requestFocus();

        Toast.makeText(CreateTheTestActivity.this, getString(R.string.insert_errorr), Toast.LENGTH_LONG).show();
    }

    private boolean validatorEditText(EditText txtCheck) {
        if (txtCheck.getText().length() < 1)    {
            txtCheck.setText("");
            txtCheck.setHint(R.string.hint_errorr_edittext);
            txtCheck.setHintTextColor(getResources().getColor(R.color.red));
            txtCheck.requestFocus();
            return false;
        }
        return true;
    }
}