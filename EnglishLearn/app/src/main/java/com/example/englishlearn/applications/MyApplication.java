package com.example.englishlearn.applications;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.example.englishlearn.R;
import com.example.englishlearn.services.AlarmUtils;
import com.example.englishlearn.sharedpreferences.DataLocalManage;

public class MyApplication extends Application {
    public static final String CHANNEL_ID = "CHANNEL_ID_LongHRK";

    @Override
    public void onCreate() {
        super.onCreate();
        DataLocalManage.init(getApplicationContext());
        createNotificationChannel();
        AlarmUtils.create(getApplicationContext(),17,5);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}