package com.example.tivi_dagatal;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Clients.IMDbClient;
import Clients.TraktClient;
import Data.DbUtils;
import Dtos.Episode;
import Dtos.Show;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        setLayout();
        fillInDates();
        
        //dummyTestEpisodes();
        //dummyTestDB();
        
        DbUtils dbHelper = new DbUtils(this);
        List<String> dataTitles = dbHelper.getOnCalShows();
        
        TraktClient trakt = new TraktClient();
        List<Episode> calendarEpisodes = trakt.getCalendarEpisodes(dataTitles);
        
        for (Episode episode : calendarEpisodes){
        	fillInEpisode(episode);
        }
    }
    
    //ey�a..
    public void dummyTestDB(){
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
    
    //ey�a..
    public void dummyTestEpisodes(){
    	Episode ep1 = new Episode();
  		ep1.setTitle("The Walking Dead");
  		ep1.setFirstAired("141006");
  		
  		Episode ep2 = new Episode();
  		ep2.setTitle("Once Upon a Time");
  		ep2.setFirstAired("141006");
  		
  		Episode ep3 = new Episode();
  		ep3.setTitle("The Big Bang Theory");
  		ep3.setFirstAired("141007");
  		
  		Episode ep4 = new Episode();
  		ep4.setTitle("New Girl");
  		ep4.setFirstAired("141008");
  		
  		Episode ep5 = new Episode();
  		ep5.setTitle("The Mindy Project");
  		ep5.setFirstAired("141008");
  		
  		Episode ep6 = new Episode();
  		ep6.setTitle("Modern Family");
  		ep6.setFirstAired("141009");
  		
  		fillInEpisode(ep1);
  		fillInEpisode(ep2);
  		fillInEpisode(ep3);
  		fillInEpisode(ep4);
  		fillInEpisode(ep5);
  		fillInEpisode(ep6);
    }
    
    public void setLayout() {
        LayoutParams lparams_wrap = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);

    	ScrollView sv = new ScrollView(this);
    	
    	LinearLayout ll = new LinearLayout(this);
    	ll.setOrientation(LinearLayout.VERTICAL);
    	ll.setId(R.id.calendar_layout);
    	
        Button btn = new Button(this);
        btn.setText(getResources().getString(R.string.temp_btn_my_episodes));
        btn.setLayoutParams(lparams_wrap);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	see_my_episodes(view);
            }
        });
    	
    	ll.addView(btn);
    	sv.addView(ll);
    	
    	setContentView(sv);
    }
    
    public void fillInDates() {
		Calendar cal = Calendar.getInstance();
		LinearLayout ll = (LinearLayout)findViewById(R.id.calendar_layout);

		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		setDateLayout(getResources().getString(R.string.sun_label), cal, ll);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		setDateLayout(getResources().getString(R.string.mon_label), cal, ll);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		setDateLayout(getResources().getString(R.string.tue_label), cal, ll);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		setDateLayout(getResources().getString(R.string.wed_label), cal, ll);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		setDateLayout(getResources().getString(R.string.thu_label), cal, ll);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		setDateLayout(getResources().getString(R.string.fri_label), cal, ll);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		setDateLayout(getResources().getString(R.string.sat_label), cal, ll);
	}

	public void setDateLayout(String day_name, Calendar cal, LinearLayout mainLayout) {		
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
		textView_month.setText(getMonthForInt(cal.get(Calendar.MONTH)));
		textView_month.setGravity(Gravity.CENTER);
		textView_month.setTextSize(10);
		
		LinearLayout ll_2 = new LinearLayout(this);
		ll_2.setOrientation(LinearLayout.VERTICAL);
		String date = new SimpleDateFormat("yyMMdd").format(cal.getTime());
		ll_2.setId(Integer.parseInt(date));
		
		//til a� athuga id
		//textView_month.setText(date);
		
		ll_1.addView(textView_name);
		ll_1.addView(textView_day);
		ll_1.addView(textView_month);
		ll.addView(ll_1);
		ll.addView(ll_2);
		
		mainLayout.addView(ll);
		mainLayout.addView(makeLine());
	}
	
	 public View makeLine(){
		 View v = new View(this);
		 v.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 1, (float) 0.80));
		 v.setBackgroundColor(Color.rgb(203,203,203));
		 return v;
	 }
	
	public String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        //String[] months = {"jan�ar","febr�ar","mars","apr�l","ma�","j�n�","j�l�","�g�st","september","okt�ber","n�vember","desember"};
        String[] months = {
        	getResources().getString(R.string.jan_label),
        	getResources().getString(R.string.feb_label),
        	getResources().getString(R.string.mar_label),
        	getResources().getString(R.string.apr_label),
        	getResources().getString(R.string.may_label),
        	getResources().getString(R.string.jun_label),
        	getResources().getString(R.string.jul_label),
        	getResources().getString(R.string.aug_label),
        	getResources().getString(R.string.sep_label),
        	getResources().getString(R.string.oct_label),
        	getResources().getString(R.string.nov_label),
        	getResources().getString(R.string.dec_label)
        };
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }
	
	public void fillInEpisode(Episode episode) {		
		String title = episode.getDataTitle();
		int ep_id = firstAiredRightForm(episode.getFirstAired());
	
		LinearLayout ll = (LinearLayout)findViewById(ep_id);
		TextView textView = new TextView(this);
	    textView.setText(title);
	    textView.setPadding(20,0,0,0);
	    try {
	    	ll.addView(textView);	
	    } catch(Exception e) {
	    	
	    }
	}
	
	public void onSearch(View view){
    	Intent intent = new Intent(this, SearchResultsActivity.class);
        startActivity(intent);
    }
	
	public void onHome(View view){
    	Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
	
	public void see_my_episodes(View view) {
		Intent intent = new Intent(this, MyShows.class);
	    startActivity(intent);
	}
    
    public int firstAiredRightForm(String strDate){
    	SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		SimpleDateFormat myFormat = new SimpleDateFormat("yyMMdd");
		String newStrDate="";
		
		try {
			newStrDate = myFormat.format(fromUser.parse(strDate));
		} catch (ParseException e) {
		    e.printStackTrace();
		}
		return Integer.parseInt(newStrDate);
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
        	// test search
        	TraktClient trakt = new TraktClient();
        	List<Show> searchShows = trakt.searchShow("big bang theory");
        	
        	// test IMDb rating
        	IMDbClient.getIMDbRating(searchShows.get(0));
        	Log.v("rating", searchShows.get(0).getImdbRating());
        	
        	// test episodes for calendar
        	List<String> calendarShows = new ArrayList<String>();
        	calendarShows.add("modern-family");
        	calendarShows.add("arrow");
        	calendarShows.add("new-girl");
        	List<Episode> calendarEpisodes = trakt.getCalendarEpisodes(calendarShows);
        	for(Episode ep : calendarEpisodes) {
        		Log.v("title and date",  ep.getTitle() + " : " + ep.getFirstAired());
        	}
        	
            return true;
        }
        //TODO: Breyta id i staekkunaglegs-takkann a lyklabordinu seinna
		if (id == R.id.search){
			onSearch(x);
			return true;
		}
		if (id == R.id.action_home){
			onHome(x);
			return true;
		}
        return super.onOptionsItemSelected(item);
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
