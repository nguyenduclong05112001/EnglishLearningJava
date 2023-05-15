package com.example.englishlearn.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.englishlearn.R;
import com.example.englishlearn.activities.HomeActivity;
import com.example.englishlearn.applications.MyApplication;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SchedulingService extends IntentService {
    public SchedulingService() {
        super(SchedulingService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Intent notificationIntent = new Intent(this, HomeActivity.class);
        notificationIntent
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        int requestID = (int) System.currentTimeMillis();
        PendingIntent contentIntent = PendingIntent
                .getActivity(this, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.layout_notification_learn_reminder);
        notificationLayout.setTextViewText(R.id.lnlr_title, getResources().getString(R.string.title_of_notification));

        String nowTime = new SimpleDateFormat("HH:mm").format(new Date());
        String info = "Now is: " + nowTime + "\nLet's finish today's lesson quickly";

        notificationLayout.setTextViewText(R.id.lnlr_info, info);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                        .setSmallIcon(R.drawable.icon)
                        .setCustomContentView(notificationLayout)
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}
