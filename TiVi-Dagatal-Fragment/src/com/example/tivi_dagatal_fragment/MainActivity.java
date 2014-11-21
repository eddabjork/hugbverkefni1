/**
 * Nafn: 		Steinunn Fridgeirsdottir
 * Dagsetning: 	20. oktober 2014
 * Markmid: 	Modurclasinn. Geymir navigation drawer og thaer
 * 				adgerdir sem tharf til ad skipta a milli fragmenta
 * 				i appinu.
 */
package com.example.tivi_dagatal_fragment;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends Activity {
    private String[] mDrawerTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private Fragment fragment;
    private static LruCache cache;
    private static long startTime = System.currentTimeMillis();
    private static int week = 0;
    
    
    //Birtir toman skja og setur nawigationDrawer upp
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cache = createCache();

        makeNavigationDrawer();        
        
        startWelcomeScreen();
    }
    
    // Notkun: cache = getCache()
    // Eftir:  cache er fratekid minnissvaedi fyrir appid
    public static LruCache getCache(){
    	return cache;
    }
    
    // Notkun: startTime = getAppStartTime()
    // Eftir:  startTime er timinn i ms sem appid byrjadi a
    public static long getAppStartTime() {
    	return startTime;
    }
    
    //Notkun: startWelcomeScreen()
    //Eftir:  buid er ad birta Start skjainn
    public void startWelcomeScreen(){
    	FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                       .replace(R.id.content_frame, new FragmentStart())
                       .commit();
    }
    //Notkun: startCal(view)
    //Eftir:  Buid er ad starta dagatalinu
    public void startCal(View view){
    	selectItem(0);
    }
    
    //Notkun: startList(view)
    //Eftir:  Buid er ad starta thaettirnir minir fragmentinu
    public void startList(View view){
    	selectItem(1);
    }
    
    //Notkun: startSearch(view)
    //Eftir:  Buid er ad starta leitar fragmentinu.
    public void startSearch(View view){
    	selectItem(2);
    }
    
    //Notkun: startPop(view)
    //Eftir:  Buid er ad starta Vinsaelir thaettir fragmentninu.
    public void startPop(View view){
    	selectItem(3);
    }
    
    //Notkun: startInfo(view)
    //Eftir:  Buid er ad starta upplysingafragmenginu.
    public void startInfo(View view){
    	selectItem(4);
    }
    
    //Notkun: makeNavigationDrawer()
    //Eftir: Buid er ad gera navigation drawer skuffur og undirföll sem 
    // 		 stjorna opnun og lokun a honum.
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
    // Eftir:  cache er plass sem hefur verid tekid fra cache-minni
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
    //Eftir: Buid ad ad setja menuinn upp
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    //Notkun: onBackPressed()
    //Eftir: Buid ad birta videigandi skjamynd eda fara til baka i appi, 
    //       eftir thvi i hvada skjamynd notandi var
    public void onBackPressed() {
    	if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){ 
            mDrawerLayout.closeDrawer(mDrawerList);
            return;}
        try{
        	FragmentCal frag = (FragmentCal)fragment;
        	doBack(0);
        	return;
        }
        catch(Exception e){}
        try{
        	FragmentSearch frag = (FragmentSearch)fragment;
        	doBack(2);
        	return;
        }
        catch(Exception e){}
        try{
        	FragmentList frag = (FragmentList)fragment;
        	doBack(1);
        	return;
        }
        catch(Exception e){}
        doBack(5);
    }
    
    //Notkun: doBack(num)
    //Eftir:  Buid er ad framkvaema back adgerdir og setja titil og 
    //        og valda skuffu retta.
    public void doBack(int num){
        String name = getResources().getString(R.string.app_name);
    	boolean onStartPage = getActionBar().getTitle().toString().equals(name);
        boolean tomurStack = getFragmentManager().getBackStackEntryCount() == 0;
        if (!onStartPage && tomurStack){
        	FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                           .replace(R.id.content_frame, new FragmentStart())
                           .commit();
            setTitle(name);
            mDrawerList.setItemChecked(5, true);
        }
    	else if (tomurStack) {
            this.finish();
        } 
    	else {
            getFragmentManager().popBackStack();
            mDrawerList.setItemChecked(num, true);
            setTitle(mDrawerTitles[num]);
        }
    }

    @Override
    //Notkun: onOptionsItemSelected(item)
    //Eftir: item hefur verid valid og samsvarandi skjar 
    //       birtur
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
	 * Nafn: 		Steinunn Friï¿½geirsdï¿½ttir
	 * Dagsetning: 	20. oktï¿½ber 2014
	 * Markmiï¿½: 	DrawerItemClickListener erfir fra OnItemClickListener og
	 * 				framkvï¿½mir aï¿½gerï¿½ir ï¿½egar notandi ï¿½tir ï¿½ hlut
	 * 				ï¿½r navigation drawer
	 */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
    	@Override
    	//Fall sem velur fragment ur navigation drawer
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    //Notkun: selectItem(int)
    //Eftir: Buid er ad velja fragment (lita tha) sem
    //       er i stadsetningu int i listanum i navigation
    //       drawer og birta thad.
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
    //Eftir:  Hjalparfall fyrir leitina
    //		  Birtir leitarnidurstodur fyrir tha leit
    //        sem er i leitarboxinu.
    public void searchClick(View view){
    	((FragmentSearch) fragment).searchClick(view);
    }

    @Override
    //Notkun: setTitle(title)
    //Eftir: Buid er ad setja titilinn i actionBar
    //		sem titil fragmentsins
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
    
    
    @Override
    //Notkun: onPrepareOptionsMenu(menu)
    //Eftir: Kallad a thegar vid kollum a invalidateOptionsMenu()
    //		 Felur thau foll sem tengjast nuverandi content view
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
    //Eftir:  Buid ad bua til skuffuna, syncar staedur i togglinu
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    //Notkun: onConfigurationChanged(newConfig)
    //Eftir:  Buid ad breyta configinu i togglinu
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    } 
    
    // Notkun: week = getWeek()
    // Eftir:  week er vikan sem er verid ad skoda i dagatali
    public static int getWeek(){
    	return week;
    }
    
    // Notkun: addWeek()
    // Eftir:  einni viku hefur verid baett vid week
    public static void addWeek(){
    	week += 1;
    }
    	
    // Notkun: subtractWeek()
    // Eftir:  ein vika hefur verid dregin fra week
    public static void subtractWeek(){
    	week -= 1;
    }
    
    // Notkun: setCurrentWeek()
    // Eftir:  week hefur verid stillt i nuverandi viku
    public static void setCurrentWeek(){
    	week = 0;
    }
}
