package com.example.tivi_dagatal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import Clients.IMDbClient;
import Clients.TraktClient;
import Data.DbUtils;
import Dtos.Episode;
import Dtos.Show;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        setLayout();
        fillInDates();
        
      //Test þættir
      		Episode ep1 = new Episode();
      		ep1.setTitle("Mindys Project");
      		ep1.setFirstAired("141005");
      		
      		Episode ep2 = new Episode();
      		ep2.setTitle("New Girl");
      		ep2.setFirstAired("141005");
      		
      		Episode ep3 = new Episode();
      		ep3.setTitle("New Girl");
      		ep3.setFirstAired("141005");
      		
      		Episode ep4 = new Episode();
      		ep4.setTitle("New Girl");
      		ep4.setFirstAired("141005");
      		
      		Episode ep5 = new Episode();
      		ep5.setTitle("New Girl");
      		ep5.setFirstAired("141005");
      		
      		Episode ep6 = new Episode();
      		ep6.setTitle("Person of Interest");
      		ep6.setFirstAired("141007");
      		
      		fillInEpisode(ep1);
      		fillInEpisode(ep2);
      		fillInEpisode(ep3);
      		fillInEpisode(ep4);
      		fillInEpisode(ep5);
      		fillInEpisode(ep6);
        
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
    
    public void setLayout() {
    	LayoutParams lparams_match = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        LayoutParams lparams_wrap = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);

    	ScrollView sv = new ScrollView(this);
    	
    	LinearLayout ll = new LinearLayout(this);
    	ll.setOrientation(LinearLayout.VERTICAL);
    	ll.setId(R.id.calendar_layout);
    	
        Button btn = new Button(this);
        btn.setText("Mínir þættir");
        btn.setLayoutParams(lparams_wrap);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	see_my_episodes(view);
            }
        });
        
    	TextView tv = new TextView(this);
    	tv.setText("halloo");
    	tv.setLayoutParams(lparams_match);
    	
    	ll.addView(btn);
    	sv.addView(ll);
    	
    	setContentView(sv);
    }
    
    public void fillInDates() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		setDateLayout("SUN", cal);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		setDateLayout("MÁN", cal);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		setDateLayout("ÞRI", cal);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		setDateLayout("MIÐ", cal);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		setDateLayout("FIM", cal);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		setDateLayout("FÖS", cal);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		setDateLayout("LAU", cal);
	}
	
	public void setDateLayout(String day_name, Calendar cal) {
		LinearLayout mainLayout = (LinearLayout)findViewById(R.id.calendar_layout);
		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setPadding(16,8,16,8);
		
		LinearLayout ll_1 = new LinearLayout(this);
		ll_1.setOrientation(LinearLayout.VERTICAL);
		TextView textView_name = new TextView(this);
		textView_name.setText(day_name);
		textView_name.setGravity(Gravity.CENTER);
		TextView textView_day = new TextView(this);
		textView_day.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
		textView_day.setGravity(Gravity.CENTER);
		textView_day.setTextSize(20);
		TextView textView_month = new TextView(this);
		//hafa með*
		//textView_month.setText(getMonthForInt(cal.get(Calendar.MONTH)));
		textView_month.setGravity(Gravity.CENTER);
		textView_month.setTextSize(10);
		
		LinearLayout ll_2 = new LinearLayout(this);
		ll_2.setOrientation(LinearLayout.VERTICAL);
		String date = new SimpleDateFormat("yyMMdd").format(cal.getTime());
		ll_2.setId(Integer.parseInt(date));
		
		//taka út*
		textView_month.setText(date);
		
		ll_1.addView(textView_name);
		ll_1.addView(textView_day);
		ll_1.addView(textView_month);
		ll.addView(ll_1);
		ll.addView(ll_2);
		
		mainLayout.addView(ll);
	}
	
	public void fillInEpisode(Episode episode) {		
		String title = episode.getTitle();
		int ep_id = Integer.parseInt(episode.getFirstAired());
		
		LinearLayout ll = (LinearLayout)findViewById(ep_id);
		TextView textView = new TextView(this);
	    textView.setText(title);
	    textView.setPadding(20,0,0,0);
	    ll.addView(textView);	
	}
	
	public void see_my_episodes(View view) {
		Intent intent = new Intent(this, MyEpisodesList.class);
	    startActivity(intent);
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
