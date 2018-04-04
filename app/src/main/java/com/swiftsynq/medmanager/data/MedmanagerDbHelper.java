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

                        MedManagerEntry.COLUMN_START_DATE    + " TEXT" + ");";

        /*
         * After we've spelled out our SQLite table creation statement above, we actually execute
         * that SQL with the execSQL method of our SQLite database object.
         */
        sqLiteDatabase.execSQL(SQL_CREATE_MEDICATION_TABLE);
    }
    /**
     * This database is only a cache for online data, so its upgrade policy is simply to discard
     * the data and call through to onCreate to recreate the table. Note that this only fires if
     * you change the version number for your database (in our case, DATABASE_VERSION). It does NOT
     * depend on the version number for your application found in your app/build.gradle file. If
     * you want to update the schema without wiping data, commenting out the current body of this
     * method should be your top priority before modifying this method.
     *
     * @param sqLiteDatabase Database that is being upgraded
     * @param oldVersion     The old database version
     * @param newVersion     The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}