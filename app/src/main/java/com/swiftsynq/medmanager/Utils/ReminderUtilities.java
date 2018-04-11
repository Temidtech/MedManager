/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.swiftsynq.medmanager.Utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.swiftsynq.medmanager.Model.Medication;
import com.swiftsynq.medmanager.Service.MedicationJobService;
import com.swiftsynq.medmanager.data.MedManagerContract;

import java.util.concurrent.TimeUnit;

public class ReminderUtilities {



    private static int REMINDER_INTERVAL_MINUTES = 0;
    private static int REMINDER_INTERVAL_SECONDS = 0;
    private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS;

    private static final String REMINDER_JOB_TAG = "medication_reminder_tag";

    private static boolean sInitialized;

    synchronized public static void scheduleMedicationReminder(@NonNull final Context context, Medication medication) {

        if (sInitialized) return;
        Bundle bundle = new Bundle();
        REMINDER_INTERVAL_MINUTES=Integer.parseInt(medication.getInterval());
        REMINDER_INTERVAL_SECONDS=(int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES));
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        bundle.putString(MedManagerContract.MedManagerEntry.COLUMN_NAME, medication.getDrugName());
        bundle.putString(MedManagerContract.MedManagerEntry.COLUMN_START_DATE, medication.getStartdate());
        bundle.putString(MedManagerContract.MedManagerEntry.COLUMN_END_DATE, medication.getEnddate());
        bundle.putString(MedManagerContract.MedManagerEntry.COLUMN_INTERVAL, medication.getInterval());
        bundle.putString(MedManagerContract.MedManagerEntry.COLUMN_DESCRIPTION, medication.getDecsription());
        Job constraintReminderJob = dispatcher.newJobBuilder()
                /* The Service that will be used to write to preferences */
                .setService(MedicationJobService.class)

                .setTag(medication.getDrugName())

                .setLifetime(Lifetime.FOREVER)

                .setRecurring(true)

                .setTrigger(Trigger.executionWindow(
                        REMINDER_INTERVAL_SECONDS,
                        REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))

                .setReplaceCurrent(true)
                .setExtras(bundle)
                .build();


        dispatcher.schedule(constraintReminderJob);

        sInitialized = true;
    }

}
