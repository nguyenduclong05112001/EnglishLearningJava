package com.example.englishlearn.services;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.example.englishlearn.sharedpreferences.DataLocalManage;

import java.util.Calendar;

public class ServiceCheckDayLearned extends JobService {

    private boolean isJobStart;
    private boolean showNotification;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        doWorkunderBackground(jobParameters);
        return true;
    }

    private void doWorkunderBackground(JobParameters jobParameters) {
        if (!showNotification){
            showNotification = true;
            return;
        }

        Calendar calendar = Calendar.getInstance();
        if (!DataLocalManage.getFirstCompleteLearn()) {

            if(isJobStart){
                return;
            }

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            AlarmUtils.create(this, hour, minute);
        }
        jobFinished(jobParameters, false);
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        isJobStart = true;
        return true;
    }
}
