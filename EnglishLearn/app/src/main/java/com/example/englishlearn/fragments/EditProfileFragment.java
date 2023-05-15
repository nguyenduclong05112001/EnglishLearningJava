package com.example.englishlearn.fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.englishlearn.R;
import com.example.englishlearn.activities.HomeActivity;
import com.example.englishlearn.api.APIConst;
import com.example.englishlearn.api.APIServer;
import com.example.englishlearn.handlerothers.RealPathUtil;
import com.example.englishlearn.models.User;
import com.example.englishlearn.sharedpreferences.DataLocalManage;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment {
    private View mView;
    private ProgressDialog mDialog;
    private HomeActivity mHomeActivity;
    private EditText txtusername, txtname, txtpassword;
    private ImageView avatar;
    private Button btnUpdate;
    private TextView lbChangepass;
    private int reqiest_code_permission = 999;
    private Uri mUri;
    private String usernameofUser;
    private boolean ispasswordTrue = false;

    private ActivityResultLauncher launch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == mHomeActivity.RESULT_OK) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        mUri = uri;
                        if (mUri != null) {
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mHomeActivity.getContentResolver(), mUri);
                                avatar.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        mappingElements();
        CallAPIgetInfomationOfUser();
        eventElements();

        return mView;
    }

    private void mappingElements() {
        mHomeActivity = (HomeActivity) getActivity();
        mDialog = new ProgressDialog(mHomeActivity);
        mDialog.setTitle(R.string.title_progess);
        mDialog.setMessage(getResources().getString(R.string.content_progess));
        mDialog.setCancelable(false);

        txtusername = mView.findViewById(R.id.fep_username);
        txtname = mView.findViewById(R.id.fep_name);
        txtpassword = mView.findViewById(R.id.fep_password);
        avatar = mView.findViewById(R.id.fep_avatar);
        lbChangepass = mView.findViewById(R.id.fep_changepass);
        btnUpdate = mView.findViewById(R.id.fap_btnedit);
    }

    private void CallAPIgetInfomationOfUser() {
        usernameofUser = DataLocalManage.getUsertoCheck();
        mDialog.show();

        APIServer.api.getInforUser(usernameofUser).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() != null) {
                    User user = response.body();
                    txtusername.setText(user.getUsername());
                    txtname.setText(user.getName());
                    txtpassword.setText(user.getPassword());
                    Glide.with(mHomeActivity).load(APIConst.baseUrl + APIConst.storeImage + user.getAvatar()).error(R.drawable.icon).into(avatar);
                    mDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(mHomeActivity, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void eventElements() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInfomation();
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckPermissionMedia();
            }
        });

        lbChangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogandCallAIP();
            }
        });
    }

    private void openDialogandCallAIP() {
        Dialog dialogconfirmexit = new Dialog(mHomeActivity);
        dialogconfirmexit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogconfirmexit.setCanceledOnTouchOutside(false);
        dialogconfirmexit.setContentView(R.layout.dialog_change_password);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogconfirmexit.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Button btnNo = dialogconfirmexit.findViewById(R.id.dcp_btnback);
        Button btnYes = dialogconfirmexit.findViewById(R.id.dcp_btnchange);
        EditText txtpassold = dialogconfirmexit.findViewById(R.id.dcp_password);
        EditText txtpassnew = dialogconfirmexit.findViewById(R.id.dcp_newpassword);
        EditText txtconfirm = dialogconfirmexit.findViewById(R.id.dcp_confirm);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogconfirmexit.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passold = txtpassold.getText().toString().trim();
                String passnew = txtpassnew.getText().toString().trim();
                String confirmpass = txtconfirm.getText().toString().trim();

                if (!validatorEditText(txtpassold) ||
                        !validatorEditText(txtpassnew) ||
                        !validatorEditText(txtconfirm)) {
                    return;
                }

                if (!passnew.equals(confirmpass)) {
                    txtconfirm.setText("");
                    validatorEditText(txtconfirm);
                    return;
                }

                if (!checkPasswordCurrent(passold)) {
                    txtpassold.setText("");
                    validatorEditText(txtpassold);
                    Toast.makeText(mHomeActivity, getString(R.string.password_wrong), Toast.LENGTH_LONG).show();
                    return;
                }
                dialogconfirmexit.dismiss();
                callAPIchangepassword(passnew);
            }
        });
        dialogconfirmexit.show();
        dialogconfirmexit.getWindow().setAttributes(lp);
    }

    private boolean checkPasswordCurrent(String passold) {
        mDialog.show();
        APIServer.api.loginHandler(usernameofUser, passold).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() != null){
                    User user = response.body();
                    if (user != null){
                        ispasswordTrue = true;
                    }else {
                        ispasswordTrue = false;
                    }
                }
                mDialog.dismiss();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(mHomeActivity, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
        return ispasswordTrue;
    }

    private void callAPIchangepassword(String passnew) {
        mDialog.show();
        APIServer.api.updatepassword(usernameofUser, passnew).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equalsIgnoreCase("Success")) {
                    Toast.makeText(mHomeActivity, getString(R.string.update_avatar_success), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mHomeActivity, getString(R.string.update_avatar_errorr), Toast.LENGTH_LONG).show();
                }
                mDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(mHomeActivity, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validatorEditText(EditText txtCheck) {
        if (txtCheck.getText().length() < 1) {
            txtCheck.setText("");
            txtCheck.setHint(R.string.hint_errorr_edittext);
            txtCheck.setHintTextColor(getResources().getColor(R.color.red));
            txtCheck.requestFocus();
            return false;
        }
        return true;
    }

    private void CheckPermissionMedia() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (mHomeActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            openAndGetImage();
        } else {
            ActivityCompat.requestPermissions(mHomeActivity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, reqiest_code_permission);
            if (mHomeActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
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

    private void updateInfomation() {
        String nameNew = txtname.getText().toString().trim();
        if (nameNew.isEmpty()) {
            txtname.requestFocus();
            txtname.setHintTextColor(getResources().getColor(R.color.red));
            return;
        }
        RequestBody username = RequestBody.create(MediaType.parse("multipart/form-data"), usernameofUser);
        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), nameNew);
        if (mUri != null) {
            String fileString = RealPathUtil.getRealPath(mHomeActivity, mUri);
            File file = new File(fileString);
            RequestBody avatar = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part avartupload = MultipartBody.Part.createFormData(APIConst.AVATAR, file.getName(), avatar);
            Log.d("TAG", "updateInfomation: " + file);
            callAPIupdateAvatar(username, name, avartupload);
        } else {
            callAPIupdatenoAvatar(username, name);
        }
    }

    private void callAPIupdatenoAvatar(RequestBody username, RequestBody name) {
        mDialog.show();
        APIServer.api.updateProfile(username, name).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equalsIgnoreCase("Success")) {
                    Toast.makeText(mHomeActivity, getString(R.string.update_avatar_success), Toast.LENGTH_LONG).show();
                    mHomeActivity.checkUsernameChanged();
                    mUri = null;
                } else {
                    Toast.makeText(mHomeActivity, getString(R.string.update_avatar_success), Toast.LENGTH_LONG).show();
                }
                mDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(mHomeActivity, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void callAPIupdateAvatar(RequestBody username, RequestBody name, MultipartBody.Part avartupload) {
        mDialog.show();
        APIServer.api.updateProfile(username, name, avartupload).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equalsIgnoreCase("Success")) {
                    Toast.makeText(mHomeActivity, getString(R.string.update_avatar_success), Toast.LENGTH_LONG).show();
                    mHomeActivity.checkUsernameChanged();
                } else {
                    Toast.makeText(mHomeActivity, getString(R.string.update_avatar_success), Toast.LENGTH_LONG).show();
                }
                mDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(mHomeActivity, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

}