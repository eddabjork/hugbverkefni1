/**
 * Nafn: 		Edda Bj�rk Konr��sd�ttir
 * Dagsetning: 	9. okt�ber 2014
 * Markmi�: 	Klasinn leyfir a� b�ta g�gnum � gagnagrunninn, s�kja g�gn
 * 				og ey�a �eim. Hj�lparklasi til a� framkv�ma a�ger�ir � 
 * 				gagnagrunninn.
 */
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
	
	//Notkun: db.getOnCalShows()
	//Eftirskilyr�i: B�i� a� er a� s�kja alla ��tti sem eru � dagatali
	//				 �r gagnagrunninum db
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
    
    //Noktun: db.getAllShows()
	//Eftirskilyr�i: B�i� er a� s�kja alla ��tti �r gagnagrunninum db
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
    
	//Noktun: db.saveShow(show)
	//Eftirskilyr�i: B�i� er a� vista ��ttinn show � gagnagrunninn db
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
    
    //Notkun: db.putShowOnCal(show)
    //Eftirskilyr�i: B�i� er a� uppf�ra gagnagrunninn db �annig a� ��tturinn
    //				 show s� � dagatalinu
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
    
    //Notkun: db.takeShowOffcal(show)
    //Eftirskilyr�i: B�i� er a� uppf�ra gagnagrunninn db �annig a�
    //				 ��tturinn show s� ekki lengur � dagatalinu
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
    
    //Notkun: db.deleteShow(show)
    //Eftirskilyr�i: B�i� er a� ey�a ��ttinum show �r gagnagrunninum db
    public void deleteShow(Show show){
    	ShowsDb showDb = new ShowsDb(this.context);
    	SQLiteDatabase db = showDb.getWritableDatabase();
    	
    	db.delete(ShowsEntry.TABLE_NAME, ShowsEntry.COLUMN_NAME_DATATITLE+"=?",
    			new String[] {show.getDataTitle()});
    	
    	return;
    }
    
    //Noktun: db.isOnCal(show)
    //Eftirskilyrði: true ef show er á dagatali í db, annars false
    public boolean isOnCal(Show show) {
    	List<String> onCalList = getOnCalShows();
    	return onCalList.contains(show.getDataTitle());
    }
}
