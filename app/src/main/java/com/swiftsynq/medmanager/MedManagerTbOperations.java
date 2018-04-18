package com.swiftsynq.medmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.swiftsynq.medmanager.Model.History;
import com.swiftsynq.medmanager.Model.Medication;
import com.swiftsynq.medmanager.data.MedManagerContract;
import com.swiftsynq.medmanager.data.MedmanagerDbHelper;

import java.util.ArrayList;
import java.util.List;

import static com.swiftsynq.medmanager.data.MedManagerContract.MedManagerEntry.COLUMN_NAME;
import static com.swiftsynq.medmanager.data.MedManagerContract.MedManagerEntry.KEY_DATE_STRING;
import static com.swiftsynq.medmanager.data.MedManagerContract.MedManagerEntry.KEY_HOUR;
import static com.swiftsynq.medmanager.data.MedManagerContract.MedManagerEntry.KEY_MINUTE;

/**
 * Created by popoolaadebimpe on 02/04/2018.
 */

public class MedManagerTbOperations {
    static MedmanagerDbHelper medmanagerDbHelper;
    static SQLiteDatabase db;
    public MedManagerTbOperations(Context context){
        // Gets the data repository in write mode
        medmanagerDbHelper = new MedmanagerDbHelper(context);
    }
    public static void openwritable() throws SQLException {
        db = medmanagerDbHelper.getWritableDatabase();
    }
    public static void openreadable() throws SQLException {
        db = medmanagerDbHelper.getReadableDatabase();
    }
    public static void close() {
        medmanagerDbHelper.close();
    }
    public static long insert(Medication medication){

        openwritable();
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
    public static long insertHistory(History history){

        openwritable();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(KEY_DATE_STRING, history.getDateString());
        values.put(MedManagerContract.MedManagerEntry.COLUMN_NAME, history.getPillName());
        values.put(KEY_HOUR, history.getHourTaken());
        values.put(KEY_MINUTE, history.getMinuteTaken());

// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(MedManagerContract.MedManagerEntry.HISTORY_TABLE_NAME, null, values);
        return newRowId;
    }
    public static List<History> getHistory() {
        List<History> allHistory = new ArrayList<>();
        String dbHist = "SELECT * FROM " + MedManagerContract.MedManagerEntry.HISTORY_TABLE_NAME;
        openreadable();
        Cursor c = db.rawQuery(dbHist, null);

        if (c.moveToFirst()) {
            do {
                History h = new History();
                h.setPillName(c.getString(c.getColumnIndex(COLUMN_NAME)));
                h.setDateString(c.getString(c.getColumnIndex(KEY_DATE_STRING)));
                h.setHourTaken(c.getInt(c.getColumnIndex(KEY_HOUR)));
                h.setMinuteTaken(c.getInt(c.getColumnIndex(KEY_MINUTE)));

                allHistory.add(h);
            } while (c.moveToNext());
        }
        c.close();
        return allHistory;
    }
    public static List<Medication> Retrieve(){

        openreadable();
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
                MedManagerContract.MedManagerEntry.COLUMN_START_DATE + " ASC";

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
    public static int Update(String istaken)
    {
        openwritable();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(MedManagerContract.MedManagerEntry.COLUMN_IS_TAKEN, istaken);

// Which row to update, based on the title
        String selection = MedManagerContract.MedManagerEntry.COLUMN_NAME + " LIKE ?";
        String[] selectionArgs = { "" };

        int count = db.update(
                MedManagerContract.MedManagerEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        return count;
    }
}
