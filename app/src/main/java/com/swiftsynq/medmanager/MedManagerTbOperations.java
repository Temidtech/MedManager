package com.swiftsynq.medmanager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.swiftsynq.medmanager.Model.Medication;
import com.swiftsynq.medmanager.data.MedManagerContract;
import com.swiftsynq.medmanager.data.MedmanagerDbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by popoolaadebimpe on 02/04/2018.
 */

public class MedManagerTbOperations {


    public static long insert(Medication medication, MedmanagerDbHelper medmanagerDbHelper){

        // Gets the data repository in write mode
        SQLiteDatabase db = medmanagerDbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MedManagerContract.MedManagerEntry.COLUMN_DESCRIPTION, medication.getDecsription());
        values.put(MedManagerContract.MedManagerEntry.COLUMN_END_DATE, medication.getEnddate());
        values.put(MedManagerContract.MedManagerEntry.COLUMN_INTERVAL, medication.getInterval());
        values.put(MedManagerContract.MedManagerEntry.COLUMN_START_DATE, medication.getStartdate());
        values.put(MedManagerContract.MedManagerEntry.COLUMN_NAME, medication.getDrugName());

// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(MedManagerContract.MedManagerEntry.TABLE_NAME, null, values);
        return newRowId;
    }
    public static List<Medication> Retrieve(MedmanagerDbHelper medmanagerDbHelper){

        SQLiteDatabase db = medmanagerDbHelper.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                MedManagerContract.MedManagerEntry.COLUMN_DESCRIPTION,
                MedManagerContract.MedManagerEntry.COLUMN_START_DATE,
                MedManagerContract.MedManagerEntry.COLUMN_END_DATE,
                MedManagerContract.MedManagerEntry.COLUMN_NAME,
                MedManagerContract.MedManagerEntry.COLUMN_INTERVAL
        };

// Filter results WHERE "title" = 'My Title'
        String selection = MedManagerContract.MedManagerEntry.COLUMN_NAME + " = ?";
       // String[] selectionArgs = { "My Title" };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                MedManagerContract.MedManagerEntry.COLUMN_START_DATE + " DESC";

        Cursor cursor = db.query(
                MedManagerContract.MedManagerEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
               null,             // The columns for the WHERE clause
               null,        // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
        List<Medication> items = new ArrayList<>();
        Medication medication=null;
        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(MedManagerContract.MedManagerEntry._ID));
            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(MedManagerContract.MedManagerEntry.COLUMN_NAME));
            String startdate = cursor.getString(
                    cursor.getColumnIndexOrThrow(MedManagerContract.MedManagerEntry.COLUMN_START_DATE));
            String enddate = cursor.getString(
                    cursor.getColumnIndexOrThrow(MedManagerContract.MedManagerEntry.COLUMN_END_DATE));
            String description = cursor.getString(
                    cursor.getColumnIndexOrThrow(MedManagerContract.MedManagerEntry.COLUMN_DESCRIPTION));
            String interval = cursor.getString(
                    cursor.getColumnIndexOrThrow(MedManagerContract.MedManagerEntry.COLUMN_INTERVAL));

            medication=new Medication();
            medication.setDecsription(description);
            medication.setDrugName(name);
            medication.setEnddate(enddate);
            medication.setInterval(interval);
            medication.setStartdate(startdate);
            items.add(medication);
        }
        cursor.close();

        return items;
    }


}
