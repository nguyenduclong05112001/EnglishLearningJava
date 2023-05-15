package com.example.englishlearn.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.englishlearn.handlerothers.ConstSendData;

import java.util.Calendar;

public class AlarmUtils {
    private static int INDEX = 0;

    public static void create(Context context, int hour, int minute) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SchedulingService.class);
        intent.putExtra(ConstSendData.KEY_TYPE, INDEX);

        PendingIntent pendingIntent =
                PendingIntent.getService(context, INDEX, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MINUTE, (minute));
        calendar.set(Calendar.HOUR_OF_DAY, hour);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager
                    .setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager
                    .set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}
