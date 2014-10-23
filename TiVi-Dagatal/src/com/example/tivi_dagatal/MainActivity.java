/*
 * Nafn: 		J�hanna Agnes Magn�sd�ttir
 * Dagsetning: 	2. okt�ber 2014
 * Markmi�: 	Stilla upp �tlit fyrir upphafsskj�, �.e. s�na viku-dagatal sem
 * 				inniheldur alla �� ��tti eru eiga a� vera � dagatali.
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
  	//Eftirskilyr�i: B�i� er a� setja upp grunnlag �tlits, sem inniheldur 
  	//				 einn takka og LinearLayout innan � ScrollView
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
  	//Eftirskilyr�i: B�i� er a� b�a til view fyrir s�rhvern dag vikunnar (alls 7) �ar
    //				 sem fram kemur dagsetning og Layout-pl�ss fyrir dagatals-��tti
    //				 �au eru svo sett inn � a�al LinearLayout � grunnlagi �tlits.
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
  	//Eftirskilyr�i: B�i� er a� b�a til view fyrir einn dag �ar sem fram kemur
    //				 dagur vikunnar, dagur m�na�ars og m�nu�urinn sj�lfur.
    //				 Einnig er b�i� a� b�a til LinearLayout inni � �essu view sem er 
    //				 me� id (dagsetningin � forminu yyMMdd) svo h�gt s� a� b�ta 
    //				 vi� ��tti � r�ttan sta�.
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
  	//Eftirskilyr�i: line er n�na view hlutur sem er einf�ld, �unn, gr� l�na.
	public View makeLine(){
		 View v = new View(this);
		 v.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 1, (float) 0.80));
		 v.setBackgroundColor(Color.rgb(203,203,203));
		 return v;
	 }
	
	//Notkun:		 month = getMonthFromInt(number);
  	//Eftirskilyr�i: month er nafn m�nu�s mi�a� vi� number �ar sem 
	//				 0=jan�ar,..,11=desember.
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
  	//Eftirskilyr�i: B�i� a� setja inn alla ��tti sem eru stilltir � "� dagatali"
	//				 � r�ttan sta� (�.e. b�ta vi� � view-i� sem hefur id-i�
	//				 dagsetninguna �egar ��tturinn var frums�ndur)
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
  	//Eftirskilyr�i: Skj�r sem inniheldur Leitar-activity-i� hefur opnast.
	public void onSearch(View view){
    	Intent intent = new Intent(this, SearchResultsActivity.class);
        startActivity(intent);
    }
	
	//Notkun:		 onHome(view);
  	//Eftirskilyr�i: Upphafsskj�r (sem inniheldur viku-dagatal) hefur opnast.
	public void onHome(View view){
    	Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
	
	//Notkun:		 seeMyEpisodes(view);
  	//Eftirskilyr�i: Skj�r sem inniheldur M�na-��tti-activity-i� hefur opnast.
	public void seeMyEpisodes(View view) {
		Intent intent = new Intent(this, MyShows.class);
	    startActivity(intent);
	}
    
	//Notkun:		 number = getFirstAiredInRightForm(strDate);
  	//Eftirskilyr�i: strDate er dagsetningu � forminu: yyyy-MM-dd'T'HH:mm:ss 
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
