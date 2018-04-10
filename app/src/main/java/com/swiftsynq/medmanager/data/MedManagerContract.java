package com.swiftsynq.medmanager.data;

/**
 * Created by popoolaadebimpe on 31/03/2018.
 */
import android.provider.BaseColumns;
public class MedManagerContract {
    public static final class MedManagerEntry implements BaseColumns {

        public static final String TABLE_NAME = "medication";

        public static final String HISTORY_TABLE_NAME = "histories";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_DESCRIPTION = "description";

        public static final String COLUMN_INTERVAL = "interval";

        public static final String COLUMN_START_DATE = "Startdate";

        public static final String COLUMN_IS_TAKEN = "istaken";

        public static final String COLUMN_END_DATE = "enddate";

        public static final String KEY_DATE_STRING = "datetaken";

        public static final String KEY_HOUR = "hour";

        public static final String KEY_MINUTE = "minute";



    }
}

