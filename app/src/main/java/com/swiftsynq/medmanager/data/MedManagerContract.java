package com.swiftsynq.medmanager.data;

/**
 * Created by popoolaadebimpe on 31/03/2018.
 */
import android.provider.BaseColumns;
public class MedManagerContract {
    public static final class MedManagerEntry implements BaseColumns {

        public static final String TABLE_NAME = "medication";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_DESCRIPTION = "description";

        public static final String COLUMN_INTERVAL = "interval";

        public static final String COLUMN_START_DATE = "Startdate";


        public static final String COLUMN_END_DATE = "enddate";

    }
}

