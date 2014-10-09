package com.example.tivi_dagatal;

import java.util.ArrayList;
import java.util.List;

import Clients.IMDbClient;
import Clients.TraktClient;

import Data.ShowsContract.ShowsEntry;
import Data.ShowsDb;
import Data.DbUtils;
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
        
        /*********** TESTA GAGNAGRUNN ************/
        
        DbUtils dbHelper = new DbUtils(this);
        
        Show show = new Show();
        show.setTitle("New Girl");
        show.setDataTitle("new-girl");
        show.setPoster("kallaposter");
        dbHelper.saveShow(show);
        
        dbHelper.takeShowOffCal(show);
        
        Show show2 = new Show();
        show2.setTitle("Big Bang Theory");
        show2.setDataTitle("big-bang-theory");
        show2.setPoster("kallaposter2");
        dbHelper.saveShow(show2);
        
        dbHelper.putShowOnCal(show2);
        
        for(Show _show: dbHelper.getAllShows()) {
        	Log.e("stuff from db - before delete", _show.getTitle());
        }
        
        for(String __show: dbHelper.getOnCalShows()) {
        	Log.e("on cal show", __show);
        }
        
        dbHelper.deleteShow(show);
        
        for(Show _show: dbHelper.getAllShows()) {
        	Log.e("stuff from db", _show.getTitle());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        
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
        	TraktClient search = new TraktClient();
        	List<Show> searchShows = search.searchShow("big bang theory");
        	Log.v("title", searchShows.get(0).getTitle());
        	IMDbClient.getIMDbRating(searchShows.get(0));
        	Log.v("rating", searchShows.get(0).getImdbRating());
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
