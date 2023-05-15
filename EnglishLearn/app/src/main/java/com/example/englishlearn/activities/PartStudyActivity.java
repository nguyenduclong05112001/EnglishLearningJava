package com.example.englishlearn.activities;

import static com.example.englishlearn.handlerothers.ConstSendData.INTENT_SEND_PART;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.englishlearn.R;
import com.example.englishlearn.activities.activitiesforlearn.ListenActivity;
import com.example.englishlearn.activities.activitiesforlearn.OptionActivity;
import com.example.englishlearn.activities.activitiesforlearn.ReadActivity;
import com.example.englishlearn.activities.activitiesforlearn.WriteActivity;
import com.example.englishlearn.api.APIConst;
import com.example.englishlearn.api.APIServer;
import com.example.englishlearn.customes.CustomeViewPagePartAdapter;
import com.example.englishlearn.handlerothers.ConstSendData;
import com.example.englishlearn.models.Level;
import com.example.englishlearn.models.Part;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartStudyActivity extends AppCompatActivity {
    private ImageView imageLevel;
    private ViewPager2 mViewPager2;
    private CustomeViewPagePartAdapter adapter;
    private List<Part> mList;
    private ProgressDialog mDialog;
    private final int reqiest_code_permission = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_study);

        checkDadatoend();
    }

    private void checkDadatoend() {
        mDialog = new ProgressDialog(this);
        mDialog.setTitle(R.string.title_progess);
        mDialog.setMessage(getResources().getString(R.string.content_progess));
        mDialog.setCancelable(false);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Level level = (Level) bundle.get(ConstSendData.LEVEL_ON_CLICK);
            if (level != null) {
                setLisPartfromLevel(level);
            }
        }
    }

    private void setLisPartfromLevel(Level level) {
        mList = new ArrayList<>();
        mDialog.show();
        APIServer.api.getPartinLevel(level.getId()).enqueue(new Callback<List<Part>>() {
            @Override
            public void onResponse(Call<List<Part>> call, Response<List<Part>> response) {
                for (Part item : response.body()) {
                    mList.add(item);
                }
                mappingElements(level.getAvatar());
                mDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Part>> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(PartStudyActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mappingElements(String avatarlevel) {
        imageLevel = findViewById(R.id.psa_image);
        Glide.with(this).load(APIConst.baseUrl + APIConst.storeImage + avatarlevel).into(imageLevel);

        mViewPager2 = findViewById(R.id.psa_viewpager2);
        adapter = new CustomeViewPagePartAdapter(mList, new CustomeViewPagePartAdapter.OnClickButtonLear() {
            @Override
            public void OnclickButtonLearn(Part part) {
                showButtonSheetDialog(part);
            }
        });
        mViewPager2.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void showButtonSheetDialog(Part part) {
        View view = getLayoutInflater().inflate(R.layout.layout_bottom_shet_part, null);
        Dialog optionDialog = new Dialog(this);
        optionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        optionDialog.setContentView(view);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(optionDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView texttitle = view.findViewById(R.id.lbsp_title);
        ImageView one = view.findViewById(R.id.lbsp_one);
        ImageView two = view.findViewById(R.id.lbsp_two);
        ImageView three = view.findViewById(R.id.lbsp_three);
        ImageView four = view.findViewById(R.id.lbsp_four);

        texttitle.setText(part.getName());

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toActivitypartseleted(part, ListenActivity.class);
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionandlisten(part);
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toActivitypartseleted(part, OptionActivity.class);
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toActivitypartseleted(part, WriteActivity.class);
            }
        });

        optionDialog.show();
        optionDialog.getWindow().setAttributes(lp);
    }

    private void toActivitypartseleted(Part part, Class<?> to) {
        Intent intent = new Intent(this, to);
        Bundle bundle = new Bundle();
        bundle.putSerializable(INTENT_SEND_PART, part);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void checkPermissionandlisten(Part part) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            toActivitypartseleted(part, ReadActivity.class);
        } else if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            toActivitypartseleted(part, ReadActivity.class);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    reqiest_code_permission);
        }
    }
}