/*
 * Nafn: 		Jóhanna Agnes Magnúsdóttir
 * Dagsetning: 	2. október 2014
 * Markmið: 	Stilla upp útlit fyrir upphafsskjá, þ.e. sýna viku-dagatal sem
 * 				inniheldur alla þá þætti eru eiga að vera á dagatali.
 */

package com.example.tivi_dagatal;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import Clients.TraktClient;
import Data.DbUtils;
import Dtos.Episode;
import Dtos.Show;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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
	/** Saves current state and sets the view */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        setLayout();
        fillInDates();
   
        new CalendarShowsTask().execute();
    }
    
    //Notkun:		 setLayout();
  	//Eftirskilyrði: Búið er að setja upp grunnlag útlits, sem inniheldur 
  	//				 einn takka og LinearLayout innan í ScrollView
    public void setLayout() {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);

    	ScrollView scrollView = new ScrollView(this);
    	
    	LinearLayout linearLayout = new LinearLayout(this);
    	linearLayout.setOrientation(LinearLayout.VERTICAL);
    	linearLayout.setId(R.id.calendar_layout);
    	
        Button button = new Button(this);
        button.setText(getResources().getString(R.string.temp_btn_my_episodes));
        button.setLayoutParams(layoutParams);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	seeMyEpisodes(view);
            }
        });
    	
    	linearLayout.addView(button);
    	scrollView.addView(linearLayout);
    	
    	setContentView(scrollView);
    }
    
    //Notkun:		 fillInDates();
  	//Eftirskilyrði: Búið er að búa til view fyrir sérhvern dag vikunnar (alls 7) þar
    //				 sem fram kemur dagsetning og Layout-pláss fyrir dagatals-þætti
    //				 Þau eru svo sett inn í aðal LinearLayout í grunnlagi útlits.
    public void fillInDates() {
		Calendar cal = Calendar.getInstance();
		LinearLayout mainLayout = (LinearLayout)findViewById(R.id.calendar_layout);

		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		setDateLayout(getResources().getString(R.string.sun_label), cal, mainLayout);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		setDateLayout(getResources().getString(R.string.mon_label), cal, mainLayout);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		setDateLayout(getResources().getString(R.string.tue_label), cal, mainLayout);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		setDateLayout(getResources().getString(R.string.wed_label), cal, mainLayout);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		setDateLayout(getResources().getString(R.string.thu_label), cal, mainLayout);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		setDateLayout(getResources().getString(R.string.fri_label), cal, mainLayout);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		setDateLayout(getResources().getString(R.string.sat_label), cal, mainLayout);
	}
    
    //Notkun:		 setDateLayout(dayName, calendar, mainLayout);
  	//Eftirskilyrði: Búið er að búa til view fyrir einn dag þar sem fram kemur
    //				 dagur vikunnar, dagur mánaðars og mánuðurinn sjálfur.
    //				 Einnig er búið að búa til LinearLayout inni í þessu view sem er 
    //				 með id (dagsetningin á forminu yyMMdd) svo hægt sé að bæta 
    //				 við þætti á réttan stað.
	public void setDateLayout(String dayName, Calendar cal, LinearLayout mainLayout) {		
		LinearLayout dayLayout = new LinearLayout(this);
		dayLayout.setOrientation(LinearLayout.HORIZONTAL);
		dayLayout.setPadding(16,8,16,8);
		
		LinearLayout dateLayout = new LinearLayout(this);
		dateLayout.setOrientation(LinearLayout.VERTICAL);
		TextView dateName = new TextView(this);
		dateName.setText(dayName);
		dateName.setGravity(Gravity.CENTER);
		TextView dateDay = new TextView(this);
		dateDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
		dateDay.setGravity(Gravity.CENTER);
		dateDay.setTextSize(20);
		TextView dateMonth = new TextView(this);
		dateMonth.setText(getMonthForInt(cal.get(Calendar.MONTH)));
		dateMonth.setGravity(Gravity.CENTER);
		dateMonth.setTextSize(10);
		dateLayout.addView(dateName);
		dateLayout.addView(dateDay);
		dateLayout.addView(dateMonth);
		
		LinearLayout episodesLayout = new LinearLayout(this);
		episodesLayout.setOrientation(LinearLayout.VERTICAL);
		String date = new SimpleDateFormat("yyMMdd").format(cal.getTime());
		episodesLayout.setId(Integer.parseInt(date));
		
		dayLayout.addView(dateLayout);
		dayLayout.addView(episodesLayout);
		
		mainLayout.addView(dayLayout);
		mainLayout.addView(makeLine());
	}
	
	//Notkun:		 line = makeLine();
  	//Eftirskilyrði: line er núna view hlutur sem er einföld, þunn, grá lína.
	public View makeLine(){
		 View v = new View(this);
		 v.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 1, (float) 0.80));
		 v.setBackgroundColor(Color.rgb(203,203,203));
		 return v;
	 }
	
	//Notkun:		 month = getMonthFromInt(number);
  	//Eftirskilyrði: month er nafn mánuðs miðað við number þar sem 
	//				 0=janúar,..,11=desember.
	public String getMonthForInt(int num) {
        String month = "wrong";
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
	
	//Notkun:		 fillInEpisode(episode);
  	//Eftirskilyrði: Búið að setja inn alla þætti sem eru stilltir á "á dagatali"
	//				 á réttan stað (þ.e. bæta við í view-ið sem hefur id-ið
	//				 dagsetninguna þegar þátturinn var frumsýndur)
	public void fillInEpisode(Episode episode) {		
		String title = episode.getShowTitle() + ": " + episode.getNumber() + " " + episode.getTitle();
		int episodeId = getFirstAiredInRightForm(episode.getFirstAired());
	
		LinearLayout linearLayout = (LinearLayout)findViewById(episodeId);
		TextView textView = new TextView(this);
	    textView.setText(title);
	    textView.setPadding(20,0,0,0);
	    try {
	    	linearLayout.addView(textView);	
	    } catch(Exception e) {
	    	
	    }
	}
	
	//Notkun:		 onSearch(view);
  	//Eftirskilyrði: Skjár sem inniheldur Leitar-activity-ið hefur opnast.
	public void onSearch(View view){
    	Intent intent = new Intent(this, SearchResultsActivity.class);
        startActivity(intent);
    }
	
	//Notkun:		 onHome(view);
  	//Eftirskilyrði: Upphafsskjár (sem inniheldur viku-dagatal) hefur opnast.
	public void onHome(View view){
    	Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
	
	//Notkun:		 seeMyEpisodes(view);
  	//Eftirskilyrði: Skjár sem inniheldur Mína-þætti-activity-ið hefur opnast.
	public void seeMyEpisodes(View view) {
		Intent intent = new Intent(this, MyShows.class);
	    startActivity(intent);
	}
    
	//Notkun:		 number = getFirstAiredInRightForm(strDate);
  	//Eftirskilyrði: strDate er dagsetningu á forminu: yyyy-MM-dd'T'HH:mm:ss 
	//				 number er talan yyMMdd.
    public int getFirstAiredInRightForm(String strDate){
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

    /** Inflate the menu; this adds items to the action bar if it is present */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        
        return true;
    }
    
    /** Handles presses on the action bar 
	 *  Parameter item: the item that was pressed on
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View x = new View(this);
    	int id = item.getItemId();
        if (id == R.id.action_settings) {       	
            return true;
        }
        
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
    	
	
	private class CalendarShowsTask extends AsyncTask<Void, Integer, Map<String, String>> {
		protected Map<String, String> doInBackground(Void... voids) {
			DbUtils dbHelper = new DbUtils(MainActivity.this);
	        Map<String, String> dataTitles = dbHelper.getOnCalShows();
			return dataTitles;
		}
		
		protected void onProgressUpdate(Integer... progress) {
			//setProgressPercent(progress[0]);
		}
		
		protected void onPostExecute(Map<String, String> dataTitles) {
			new CalendarEpisodesTask().execute(dataTitles);
		}
	}
	
	private class CalendarEpisodesTask extends AsyncTask<Map<String, String>, Integer, List<Episode>> {
		protected List<Episode> doInBackground(Map<String, String>... dataTitles) {         
			TraktClient trakt = new TraktClient();
	        List<Episode> calendarEpisodes = trakt.getCalendarEpisodes(dataTitles[0]);
			return calendarEpisodes;
		}
		
		protected void onProgressUpdate(Integer... progress) {
			//setProgressPercent(progress[0]);
		}
		
		protected void onPostExecute(List<Episode> calendarEpisodes) {
			for (Episode episode : calendarEpisodes){
	        	fillInEpisode(episode);
	        }
		}
	}
}
