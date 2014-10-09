package Data;

import java.util.ArrayList;
import java.util.List;

import Data.ShowsContract.ShowsEntry;
import Dtos.Show;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbUtils {
	private Context context;
	
	public DbUtils(Context context) {
		this.context = context;
	}
	
	public List<String> getOnCalShows() {
    	ShowsDb showsdb = new ShowsDb(this.context);
    	SQLiteDatabase db = showsdb.getWritableDatabase();
    	
    	Cursor cursor = db.query(
        		true,
        		ShowsEntry.TABLE_NAME,
        		null,
        		ShowsEntry.COLUMN_NAME_ONCAL+"=?", //where
        		new String[] {"1"}, //where arguments
        		null,
        		null,
        		null,
        		null);
        
        List<String> results = new ArrayList<String>();
        
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            results.add(cursor.getString(cursor.getColumnIndex(ShowsEntry.COLUMN_NAME_DATATITLE)));
        }
    	return results;
    }
    
    public List<Show> getAllShows() {
    	ShowsDb showsdb = new ShowsDb(this.context);
    	SQLiteDatabase db = showsdb.getWritableDatabase();
    	
        Cursor cursor = db.query(
        		true,
        		ShowsEntry.TABLE_NAME,
        		null,
        		null,
        		null,
        		null,
        		null,
        		null,
        		null);
        
        List<Show> results = new ArrayList<Show>();
        
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Show show = new Show();
            show.setTitle(cursor.getString(cursor.getColumnIndex(ShowsEntry.COLUMN_NAME_TITLE)));
            show.setDataTitle(cursor.getString(cursor.getColumnIndex(ShowsEntry.COLUMN_NAME_DATATITLE)));
            show.setPoster(cursor.getString(cursor.getColumnIndex(ShowsEntry.COLUMN_NAME_POSTER)));
            results.add(show);
        }
        
    	return results;
    }
    
    public void saveShow(Show show) {
    	ShowsDb showDb = new ShowsDb(this.context);
    	SQLiteDatabase db = showDb.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	
    	values.put(ShowsEntry.COLUMN_NAME_TITLE, show.getTitle());
    	values.put(ShowsEntry.COLUMN_NAME_DATATITLE, show.getDataTitle());
    	values.put(ShowsEntry.COLUMN_NAME_ONCAL, false);
    	values.put(ShowsEntry.COLUMN_NAME_POSTER, show.getPoster());
    	db.insert(ShowsEntry.TABLE_NAME, ShowsEntry.COLUMN_NAME_NULLABLE, values);
    	return;
    }
    
    public void putShowOnCal(Show show) {
    	ShowsDb showsdb = new ShowsDb(this.context);
    	SQLiteDatabase db = showsdb.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	
    	values.put(ShowsEntry.COLUMN_NAME_ONCAL, true);
    	
    	db.update(ShowsEntry.TABLE_NAME, values, 
    			ShowsEntry.COLUMN_NAME_DATATITLE+"=?", 
    			new String[] {show.getDataTitle()});
    	
    	return;
        
    }
    
    public void takeShowOffCal(Show show) {
    	ShowsDb showsdb = new ShowsDb(this.context);
    	SQLiteDatabase db = showsdb.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	
    	values.put(ShowsEntry.COLUMN_NAME_ONCAL, false);
    	
    	db.update(ShowsEntry.TABLE_NAME, values, 
    			ShowsEntry.COLUMN_NAME_DATATITLE+"=?", 
    			new String[] {show.getDataTitle()});
    	
    	return;
        
    }
    
    public void deleteShow(Show show){
    	ShowsDb showDb = new ShowsDb(this.context);
    	SQLiteDatabase db = showDb.getWritableDatabase();
    	
    	db.delete(ShowsEntry.TABLE_NAME, ShowsEntry.COLUMN_NAME_DATATITLE+"=?",
    			new String[] {show.getDataTitle()});
    	
    	return;
    }
}
