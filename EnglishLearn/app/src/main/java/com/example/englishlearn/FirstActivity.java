package com.example.englishlearn;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.englishlearn.activities.HomeActivity;
import com.example.englishlearn.activities.LoginActivity;
import com.example.englishlearn.api.APIServer;
import com.example.englishlearn.broadcastreceivers.BroadcastReceiverInternet;
import com.example.englishlearn.models.InfoPoinOfDay;
import com.example.englishlearn.services.ServiceCheckDayLearned;
import com.example.englishlearn.sharedpreferences.DataLocalManage;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstActivity extends AppCompatActivity {
    private static final int JOB_ID = 1000;
    private BroadcastReceiverInternet broadcastReceiver;
    private int point_yesterday = 0;
    private int point_sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        broadcastReceiver = new BroadcastReceiverInternet();
        resetPoinofday();
        scheduleResetPointofday();

        if (point_yesterday == 0) {
            callAPIupdateAchievement(
                    DataLocalManage.getUsernameLogined(),
                    DataLocalManage.getPointofday().getPoint(),
                    DataLocalManage.getPointofday().getChains()
            );
        } else {
            callAPIupdateAchievement(
                    DataLocalManage.getUsernameLogined(),
                    DataLocalManage.getPointofday().getPoint(),
                    DataLocalManage.getPointofday().getChains(),
                    DataLocalManage.getPointofday().getPointofweek()
            );
        }
        waitAndToActivity();
    }

    private void resetPoinofday() {
        String today = new SimpleDateFormat("dd:MM:yyyy").format(new Date());

        if (DataLocalManage.getPointofday() == null) {
            InfoPoinOfDay infoPoinOfDay = new InfoPoinOfDay(today, 0, 0, 0);
            DataLocalManage.setPointofday(infoPoinOfDay);
            DataLocalManage.setFirstCompleteLearn(false);
            return;
        }

        if (DataLocalManage.getPointofday().getDay().equalsIgnoreCase(today)) {
            if (DataLocalManage.getPointofday().getPoint() == 0) {
                DataLocalManage.setFirstCompleteLearn(false);
            }
            return;
        } else {
            if (DataLocalManage.getPointofday().getPoint() != 0) {
                int chain = DataLocalManage.getPointofday().getChains();
                point_sum = DataLocalManage.getPointofday().getPoint() + DataLocalManage.getPointofday().getPointofweek();
                point_yesterday = DataLocalManage.getPointofday().getPoint();
                InfoPoinOfDay infoPoinOfDay = new InfoPoinOfDay(today, 0, chain, point_yesterday);
                DataLocalManage.setPointofday(infoPoinOfDay);
                DataLocalManage.setFirstCompleteLearn(false);
                return;
            } else {
                point_sum = DataLocalManage.getPointofday().getPoint() + DataLocalManage.getPointofday().getPointofweek();
                InfoPoinOfDay infoPoinOfDay = new InfoPoinOfDay(today, 0, 0, point_yesterday);
                DataLocalManage.setPointofday(infoPoinOfDay);
                DataLocalManage.setFirstCompleteLearn(false);
                return;
            }
        }
    }

    private void scheduleResetPointofday() {
        ComponentName componentName = new ComponentName(this, ServiceCheckDayLearned.class);
        JobInfo jobInfo = new JobInfo.Builder(JOB_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(5 * 60 * 60 * 100)
                .setPersisted(true)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
    }

    private void waitAndToActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (DataLocalManage.getIsLogin()) {
                    toActivity(HomeActivity.class);
                } else {
                    toActivity(LoginActivity.class);
                }
            }
        }, 2000);
    }

    private void toActivity(Class<?> to) {
        Intent intent = new Intent(this, to);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void callAPIupdateAchievement(String usernaem
            , int point
            , int chain) {
        APIServer.api.updateAchievementOfUser(usernaem, point, chain)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(FirstActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void callAPIupdateAchievement(String usernaem
            , int point
            , int chain
            , int ointOld) {
        APIServer.api.updateAchievementOfUser(usernaem, point, chain, ointOld)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(FirstActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}