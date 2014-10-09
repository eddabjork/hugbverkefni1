package com.example.tivi_dagatal;

import java.util.ArrayList;
import java.util.List;

import Clients.IMDbClient;
import Clients.TraktClient;

import Data.ShowsContract.ShowsEntry;
import Data.ShowsDb;
import Dtos.Episode;
import Dtos.Show;
import android.support.v7.app.ActionBarActivity;

import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;



public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        /*********** TESTA GAGNAGRUNN ************/
        //skrifa í gagnagrunn
        ShowsDb mDbHelper = new ShowsDb(getBaseContext());
        
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ShowsEntry.COLUMN_NAME_TITLE, "The Big Bang Theory");
        values.put(ShowsEntry.COLUMN_NAME_DATATITLE, "the-big-bang-theory");
        long newRowId = db.insert(ShowsEntry.TABLE_NAME, ShowsEntry.COLUMN_NAME_NULLABLE, values);
        
        //sækja úr grunni
        SQLiteDatabase dbRead = mDbHelper.getReadableDatabase();
        
        String[] columns = {
        		ShowsEntry._ID,
        		ShowsEntry.COLUMN_NAME_TITLE,
        		ShowsEntry.COLUMN_NAME_DATATITLE
        };
        
        String[] attributes = {
        		"The Big Bang Theory"
        };
        
        Cursor cursor = db.query(
        		ShowsEntry.TABLE_NAME,
        		columns,
        		ShowsEntry.COLUMN_NAME_TITLE +"=?", //where
        		attributes, //value for where
        		null,
        		null,
        		null);
        
        cursor.moveToFirst();
        //set gögnin í data breytuna -> titill fer í data
        String data = cursor.getString(
            cursor.getColumnIndex(ShowsEntry.COLUMN_NAME_TITLE)
        );
        Log.e("stuff from db", data); //logga og athuga hvort data skili sér
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        View x = new View(this);
    	int id = item.getItemId();
        if (id == R.id.action_settings) {
        	// test search
        	TraktClient trakt = new TraktClient();
        	List<Show> searchShows = trakt.searchShow("big bang theory");
        	Log.v("title", searchShows.get(0).getTitle());
        	
        	// test IMDb rating
        	IMDbClient.getIMDbRating(searchShows.get(0));
        	Log.v("rating", searchShows.get(0).getImdbRating());
        	
        	// test episodes for calendar
        	List<String> calendarShows = new ArrayList<String>();
        	calendarShows.add("friends");
        	List<Episode> calendarEpisodes = trakt.getCalendarEpisodes(calendarShows);
        	for(Episode ep : calendarEpisodes){
        		Log.v("ep", ep.getTitle());
        	}
            return true;
        }
        //TODO: Breyta id i staekkunaglegs-takkann a lyklabordinu seinna
		if (id == R.id.search){
			onSearch(x);
			return true;
		}
        return super.onOptionsItemSelected(item);
    }
    
	public void onSearch(View view){
    	Intent intent = new Intent(this, SearchResultsActivity.class);
        startActivity(intent);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
}
