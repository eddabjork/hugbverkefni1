/**
 * Nafn: 		Edda Bjork Konradsdottir
 * Dagsetning: 	4. oktober 2014
 * Markmid: 	Bua til gagnagrunn fyrir thaetti sem eiga ad vera
 * 				i listanum thaettirnir minir.
 */
package Data;

import Data.ShowsContract.ShowsEntry;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ShowsDb extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "Shows.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String BOOL_TYPE = " BOOLEAN";
    private static final String COMMA_SEP = ",";
    private static final String NOT_NULL = " NOT NULL";
    private static final String DEFAULT_FALSE = " DEFAULT false";
    private static final String SQL_CREATE_ENTRIES =
		"CREATE TABLE " + ShowsEntry.TABLE_NAME + " (" +
		ShowsEntry._ID + " INTEGER PRIMARY KEY," +
		ShowsEntry.COLUMN_NAME_DATATITLE + TEXT_TYPE + NOT_NULL + COMMA_SEP +
		ShowsEntry.COLUMN_NAME_TITLE + TEXT_TYPE + NOT_NULL + COMMA_SEP +
		ShowsEntry.COLUMN_NAME_ONCAL + BOOL_TYPE + NOT_NULL + DEFAULT_FALSE + COMMA_SEP +
		ShowsEntry.COLUMN_NAME_POSTER + TEXT_TYPE + COMMA_SEP +
		"UNIQUE(" + ShowsEntry.COLUMN_NAME_DATATITLE + 
		COMMA_SEP + ShowsEntry.COLUMN_NAME_TITLE + ") ON CONFLICT IGNORE)";

    private static final String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS " + ShowsEntry.TABLE_NAME;
    
	public ShowsDb(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	//Eftirskilyrdi: Buid er ad bua til gagnagrunninn db 
	public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
	 
    //Eftirskilyrdi: Buid er ad uppfaera gagnagrunninn db ur oldVersion
	//				 i newVersion
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
