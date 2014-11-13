/**
 * Nafn: 		Steinunn Friðgeirsdóttir
 * Dagsetning: 	20. október 2014
 * Markmið: 	Móðurclasinn. Geymir navigation drawer og þær
 * 				aðgerðir sem þarf til að skipta á milli fragmenta
 * 				í appinu.
 */
package com.example.tivi_dagatal_fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Clients.TraktClient;
import Data.DbUtils;
import Dtos.Episode;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.LruCache;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;


public class MainActivity extends Activity {
    private String[] mDrawerTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private Fragment fragment;
    public static LruCache cache;
    public static long startTime = System.currentTimeMillis();
    private static int week = 0;
    
    
    //Birtir tóman skjá og setur nawigationDrawer upp
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cache = createCache();

        makeNavigationDrawer();        
        
        selectItem(0);
    }
    
    //Notkun: makeNavigationDrawer()
    //Eftir: Búið er að gera navigation drawer og undirföll sem 
    // 		 stjórna opnun og lokun á honum.
    public void makeNavigationDrawer(){
        mDrawerTitles = getResources().getStringArray(R.array.drawer_title_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mDrawerTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
     
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            // Called when a drawer has settled in a completely closed state. 
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            //Called when a drawer has settled in a completely open state. 
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  // host Activity 
                mDrawerLayout,         // DrawerLayout object 
                R.drawable.ic_drawer,  // nav drawer icon to replace 'Up' caret 
                R.string.drawer_open,  // "open drawer" description 
                R.string.drawer_close  // "close drawer" description 
                ) {

            // Called when a drawer has settled in a completely closed state. 
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
            }

            // Called when a drawer has settled in a completely open state. 
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }
    
    // Notkun: cache = createCache()
    // Eftir:  cache er pláss sem hefur verið tekið frá í cache-minni
    public LruCache createCache(){
		// Get memory class of this device, exceeding this amount will throw an OutOfMemory exception.
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		int memClass = am.getMemoryClass();			
		// Use 1/8th of the available memory for this memory cache.
	    final int cacheSize = 1024 * 1024 * memClass / 8;
	    cache = new LruCache<String, List<Object>>(cacheSize);	    
	    return cache;
	}
    
    @Override
    //Notkun: onCreateOptionsMenu(menu)
    //Eftir: Búið að að setja menuinn upp
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }
    
    //@Override
	/*public void onBackPressed() {
		FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                       .replace(R.id.content_frame, new FragmentCal())
                       .commit();
        setTitle(R.string.calendar);
	}*/

    
    @Override
    //Notkun: onOptionsItemSelected(item)
    //Eftir: item hefur verið valið og samsvarandi skjár 
    //       birtur (enginn eins og er)
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    	if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
          }
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /**
	 * Nafn: 		Steinunn Friðgeirsdóttir
	 * Dagsetning: 	20. október 2014
	 * Markmið: 	DrawerItemClickListener erfir frá OnItemClickListener og
	 * 				framkvæmir aðgerðir þegar notandi ýtir á hlut
	 * 				úr navigation drawer
	 */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
    	@Override
    	//Fall sem velur fragment úr navigation drawer
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    //Notkun: selectItem(int)
    //Eftir: Búið er að velja fragment (lita það) sem
    //       er á staðsetningu int í listanum í navigation
    //       drawer og birta það.
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        switch (position) {
        case 0:
            fragment = new FragmentCal();
            break;
        case 1:
            fragment = new FragmentList();
            break;
        case 2:
            fragment = new FragmentSearch();
            break;
        case 3:
            fragment = new FragmentPopular();
            break; 
        case 4:
        	fragment = new FragmentInfo();
        	break;
        default:
            break;
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                       .replace(R.id.content_frame, fragment)
                       .commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mDrawerTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }
    
    //Notkun: searchClick(view)
    //Eftir:  Hjálparfall fyrir leitina
    //		  Birtir leitarniðurstöður fyrir þá leit
    //        sem er í leitarboxinu.
    public void searchClick(View view){
    	((FragmentSearch) fragment).searchClick(view);
    }

    @Override
    //Notkun: setTitle(title)
    //Eftir: Búið er að setja titilinn í actionBar
    //		sem titil fragmentsins
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
    
    
    @Override
    //Notkun: onPrepareOptionsMenu(menu)
    //Eftir: Kallað á þegar við köllum á invalidateOptionsMenu()
    //		 Felur þau föll sem tengjast núverandi content view (ekki notað í bili)
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
    	// hide the 'three dots' menu
    	MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);
    	return super.onPrepareOptionsMenu(menu);
    }
	
	@Override
	//Notkun: onPostCreate(savedInstanceState)
    //Eftir:  Búið að búa til skúffuna, syncar stöður í togglinu
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    //Notkun: onConfigurationChanged(newConfig)
    //Eftir:  Búið að breyta configinu í togglinu
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    } 
    
    // Notkun: week = getWeek()
    // Eftir:  week er vikan sem er verið að skoða á dagatali
    public static int getWeek(){
    	return week;
    }
    
    // Notkun: addWeek()
    // Eftir:  einni viku hefur verið bætt við week
    public static void addWeek(){
    	week += 1;
    }
    	
    // Notkun: subtractWeek()
    // Eftir:  ein vika hefur verið dregin frá week
    public static void subtractWeek(){
    	week -= 1;
    }
    
    // Notkun: setCurrentWeek()
    // Eftir:  week hefur verið stillt á núverandi viku
    public static void setCurrentWeek(){
    	week = 0;
    }
}
