package Data;

import android.provider.BaseColumns;

public final class ShowsContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public ShowsContract() {}

    /* Inner class that defines the table contents */
    public static abstract class ShowsEntry implements BaseColumns {
        public static final String TABLE_NAME = "shows";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DATATITLE = "datatitle";
        public static final String COLUMN_NAME_NULLABLE = "NOT NULL";
        public static final String COLUMN_NAME_ONCAL = "on_calendar";
        public static final String COLUMN_NAME_POSTER = "poster";
        //...
    }
}
