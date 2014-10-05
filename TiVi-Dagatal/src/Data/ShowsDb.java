package Data;

import Data.ShowsContract.ShowsEntry;
import android.database.sqlite.*;
import android.content.Context;

public class ShowsDb extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Shows.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
		"CREATE TABLE " + ShowsEntry.TABLE_NAME + " (" +
		ShowsEntry._ID + " INTEGER PRIMARY KEY," +
		ShowsEntry.COLUMN_NAME_DATATITLE + TEXT_TYPE + COMMA_SEP +
		ShowsEntry.COLUMN_NAME_TITLE + TEXT_TYPE +
		//  // Any other options for the CREATE command
		" )";

    private static final String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS " + ShowsEntry.TABLE_NAME;
    
	public ShowsDb(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	 public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
	 
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
