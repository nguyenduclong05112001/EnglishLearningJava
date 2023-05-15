package com.example.englishlearn.activities;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.englishlearn.R;
import com.example.englishlearn.api.APIServer;
import com.example.englishlearn.handlerothers.ConstSendData;
import com.example.englishlearn.models.InfoPoinOfDay;
import com.example.englishlearn.sharedpreferences.DataLocalManage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteJobActivity extends AppCompatActivity {

    private Button btnNext;
    private TextView pointofyou;
    private MediaPlayer mediaPlayer;
    private int your_point_get = 0;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_job);
        mappingElements();
        eventElements();
    }

    private void eventElements() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDataUpServer();
            }
        });
    }

    private void sendDataUpServer() {
        if (!DataLocalManage.getFirstCompleteLearn()) {
            int chain = DataLocalManage.getPointofday().getChains() + 1;
            InfoPoinOfDay infoPoinOfDay = DataLocalManage.getPointofday();
            infoPoinOfDay.setChains(chain);
            infoPoinOfDay.setPoint(your_point_get);
            infoPoinOfDay.setPointofweek(DataLocalManage.getPointofday().getPointofweek() + your_point_get);
            DataLocalManage.setPointofday(infoPoinOfDay);
            DataLocalManage.setFirstCompleteLearn(true);
        } else {
            InfoPoinOfDay infoPoinOfDay = DataLocalManage.getPointofday();
            int pointsum = infoPoinOfDay.getPoint() + your_point_get;
            infoPoinOfDay.setPoint(pointsum);
            infoPoinOfDay.setPointofweek(DataLocalManage.getPointofday().getPointofweek() + your_point_get);
            DataLocalManage.setPointofday(infoPoinOfDay);
        }

        //call api save data
        InfoPoinOfDay infoUpdate = DataLocalManage.getPointofday();
        String usernameUpdate = DataLocalManage.getUsernameLogined();

        mDialog.show();
        APIServer.api.updateAchievementOfUser(
                usernameUpdate,
                infoUpdate.getPoint(),
                infoUpdate.getChains()
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body().equalsIgnoreCase("Success")){
                    new HomeActivity().checkUsernameChanged();
                }else {
                    Toast.makeText(CompleteJobActivity.this, getString(R.string.update_avatar_errorr), Toast.LENGTH_LONG).show();
                }
                mDialog.dismiss();
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(CompleteJobActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void mappingElements() {
        mDialog = new ProgressDialog(this);
        mDialog.setTitle(R.string.title_progess);
        mDialog.setMessage(getResources().getString(R.string.content_progess));
        mDialog.setCancelable(false);
        btnNext = findViewById(R.id.ca_btnNext);
        pointofyou = findViewById(R.id.ca_pointofyou);
        checkPointOfUser();
        mediaPlayer = MediaPlayer.create(this, R.raw.complete_learn);
        mediaPlayer.start();
    }

    private void checkPointOfUser() {
        int pointofuser = getIntent().getIntExtra(ConstSendData.INTENT_SEND_POIN_OF_LEARN, 0);
        if (pointofuser != 0) {
            your_point_get = pointofuser;
            String strPoint = getResources().getString(R.string.you_got) + " " + pointofuser + " points";
            pointofyou.setText(strPoint);
        }
    }

    @Override
    protected void onStop() {
        if (mediaPlayer.isPlaying() || mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onStop();
    }
}