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

import Clients.TraktClient;
import Data.DbUtils;
import Dtos.Episode;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
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
        
        DbUtils dbHelper = new DbUtils(this);
        List<String> dataTitles = dbHelper.getOnCalShows();
        
        TraktClient trakt = new TraktClient();
        List<Episode> calendarEpisodes = trakt.getCalendarEpisodes(dataTitles);
        
        for (Episode episode : calendarEpisodes){
        	fillInEpisode(episode);
        }
    }
    
    //Notkun:		 setLayout();
  	//Eftirskilyrði: Búið er að setja upp grunnlag útlits, sem inniheldur 
  	//				 einn takka og LinearLayout innan í ScrollView
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
    
    //Notkun:		 fillInDates();
  	//Eftirskilyrði: Búið er að búa til view fyrir sérhvern dag vikunnar (alls 7) þar
    //				 sem fram kemur dagsetning og Layout-pláss fyrir dagatals-þætti
    //				 Þau eru svo sett inn í aðal LinearLayout í grunnlagi útlits.
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
    
    //Notkun:		 setDateLayout(day_name, calendar, mainLayout);
  	//Eftirskilyrði: Búið er að búa til view fyrir einn dag þar sem fram kemur
    //				 dagur vikunnar, dagur mánaðars og mánuðurinn sjálfur.
    //				 Einnig er búið að búa til LinearLayout inni í þessu view sem er 
    //				 með id (dagsetningin á forminu yyMMdd) svo hægt sé að bæta 
    //				 við þætti á réttan stað.
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
		
		ll_1.addView(textView_name);
		ll_1.addView(textView_day);
		ll_1.addView(textView_month);
		ll.addView(ll_1);
		ll.addView(ll_2);
		
		mainLayout.addView(ll);
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
        DateFormatSymbols dfs = new DateFormatSymbols();
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
	
	//Notkun:		 see_my_episodes(view);
  	//Eftirskilyrði: Skjár sem inniheldur Mína-þætti-activity-ið hefur opnast.
	public void see_my_episodes(View view) {
		Intent intent = new Intent(this, MyShows.class);
	    startActivity(intent);
	}
    
	//Notkun:		 number = firstAiredRightForm(strDate);
  	//Eftirskilyrði: strDate er dagsetningu á forminu: yyyy-MM-dd'T'HH:mm:ss 
	//				 number er talan yyMMdd.
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
}
