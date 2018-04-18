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
package com.swiftsynq.medmanager.Service;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.swiftsynq.medmanager.MedManagerTbOperations;
import com.swiftsynq.medmanager.Model.History;
import com.swiftsynq.medmanager.R;
import com.swiftsynq.medmanager.Utils.NotificationUtils;
import com.swiftsynq.medmanager.data.MedManagerContract;
import com.swiftsynq.medmanager.data.MedmanagerDbHelper;

import java.text.DateFormatSymbols;
import java.util.Date;

public class ReminderTasks {

    public static final String ACTION_ADD_MEDICATION_HISTORY = "add-medication-history";
    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";
    static final String ACTION_MEDICATION_REMINDER = "medication-reminder";

    public static void executeTask(Context context, String action, Bundle jobParameters) {
        if (ACTION_ADD_MEDICATION_HISTORY.equals(action)) {
            addMedicationHistory(context,jobParameters);
        } else if (ACTION_DISMISS_NOTIFICATION.equals(action)) {
            NotificationUtils.clearAllNotifications(context);
        } else if (ACTION_MEDICATION_REMINDER.equals(action)) {
            issueMedicationReminder(context,jobParameters);
        }
    }

    private static void addMedicationHistory(Context context,Bundle bundle) {
        MedmanagerDbHelper medmanagerDbHelper= new MedmanagerDbHelper(context);
        Date now = new Date();
        History history=new History();
        history.setPillName(bundle.getString(MedManagerContract.MedManagerEntry.COLUMN_NAME));
        history.setDateString(context.getString(R.string.calendar_date_picker_result_values, getStringMonthTime(now.getMonth()),now.getDay(),now.getYear()));
        history.setHourTaken(now.getHours());
        history.setMinuteTaken(now.getMinutes());
        Log.d("History",String.valueOf(MedManagerTbOperations.insertHistory(history)));

        NotificationUtils.clearAllNotifications(context);
    }

    private static String getStringMonthTime(int month) {
        String monthString;
        monthString = new DateFormatSymbols().getMonths()[month];
        return monthString;
    }
    private static void issueMedicationReminder(Context context,Bundle bundle) {
                //PreferenceUtilities.incrementChargingReminderCount(context);
        NotificationUtils.remindUserMedication(context,bundle);
    }
}