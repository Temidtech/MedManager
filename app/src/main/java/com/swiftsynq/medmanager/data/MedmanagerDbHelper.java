package com.swiftsynq.medmanager.data;

/**
 * Created by popoolaadebimpe on 30/03/2018.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.swiftsynq.medmanager.data.MedManagerContract.MedManagerEntry;

public class MedmanagerDbHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "medmanager.db";


    private static final int DATABASE_VERSION = 1;

    public MedmanagerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /*
         * This String will contain a simple SQL statement that will create a table that will
         * cache our weather data.
         */
        final String SQL_CREATE_MEDICATION_TABLE =

                "CREATE TABLE " + MedManagerEntry.TABLE_NAME + " (" +


                        MedManagerEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        MedManagerEntry.COLUMN_DESCRIPTION       + " TEXT, "                 +

                        MedManagerEntry.COLUMN_INTERVAL + " INTEGER, "                 +

                        MedManagerEntry.COLUMN_NAME   + " TEXT, "                    +

                        MedManagerEntry.COLUMN_END_DATE   + " TEXT, "                    +

                        MedManagerEntry.COLUMN_IS_TAKEN   + " TEXT, "                    +

                        MedManagerEntry.COLUMN_START_DATE    + " TEXT" + ");";

        /** Histories Table: create statement */
         final String CREATE_HISTORIES_TABLE =
                "CREATE TABLE "             + MedManagerEntry.HISTORY_TABLE_NAME + "("
                        + MedManagerEntry._ID         + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + MedManagerEntry.COLUMN_NAME      + " text not null, "
                        + MedManagerEntry.KEY_DATE_STRING   + " text, "
                        + MedManagerEntry.KEY_HOUR          + " integer, "
                        + MedManagerEntry.KEY_MINUTE        + " integer " + ")";


        sqLiteDatabase.execSQL(SQL_CREATE_MEDICATION_TABLE);
        sqLiteDatabase.execSQL(CREATE_HISTORIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}