package com.swiftsynq.medmanager.Service;

/**
 * Created by popoolaadebimpe on 05/04/2018.
 */
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
        import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;
import com.swiftsynq.medmanager.Utils.ReminderUtilities;

public class MedicationJobService extends JobService {
    private AsyncTask mBackgroundTask;
    private  String Medication;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {


        mBackgroundTask = new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {
                Context context = MedicationJobService.this;
                Bundle bundle=jobParameters.getExtras();
                ReminderTasks.executeTask(context, ReminderTasks.ACTION_MEDICATION_REMINDER,bundle);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {

                jobFinished(jobParameters, false);
            }
        };
        mBackgroundTask.execute();
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
        return true;
    }
}