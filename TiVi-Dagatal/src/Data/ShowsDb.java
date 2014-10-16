/**
 * Nafn: 		Edda Björk Konráðsdóttir
 * Dagsetning: 	4. október 2014
 * Markmið: 	Búa til gagnagrunn fyrir þætti sem eiga að vera
 * 				í listanum Þættirnir Mínir.
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
	
	//Eftirskilyrði: Búið er að búa til gagnagrunninn db 
	public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
	 
    //Eftirskilyrði: Búið er að uppfæra gagnagrunninn db úr oldVersion
	//				 í newVersion
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
