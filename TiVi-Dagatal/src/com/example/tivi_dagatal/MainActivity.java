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
  	//Eftirskilyr�i: B�i� er a� setja upp grunnlag �tlits, sem inniheldur 
  	//				 einn takka og LinearLayout innan � ScrollView
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
  	//Eftirskilyr�i: B�i� er a� b�a til view fyrir s�rhvern dag vikunnar (alls 7) �ar
    //				 sem fram kemur dagsetning og Layout-pl�ss fyrir dagatals-��tti
    //				 �au eru svo sett inn � a�al LinearLayout � grunnlagi �tlits.
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
  	//Eftirskilyr�i: B�i� er a� b�a til view fyrir einn dag �ar sem fram kemur
    //				 dagur vikunnar, dagur m�na�ars og m�nu�urinn sj�lfur.
    //				 Einnig er b�i� a� b�a til LinearLayout inni � �essu view sem er 
    //				 me� id (dagsetningin � forminu yyMMdd) svo h�gt s� a� b�ta 
    //				 vi� ��tti � r�ttan sta�.
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
  	//Eftirskilyr�i: B�i� a� setja inn alla ��tti sem eru stilltir � "� dagatali"
	//				 � r�ttan sta� (�.e. b�ta vi� � view-i� sem hefur id-i�
	//				 dagsetninguna �egar ��tturinn var frums�ndur)
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
	
	//Notkun:		 see_my_episodes(view);
  	//Eftirskilyr�i: Skj�r sem inniheldur M�na-��tti-activity-i� hefur opnast.
	public void see_my_episodes(View view) {
		Intent intent = new Intent(this, MyShows.class);
	    startActivity(intent);
	}
    
	//Notkun:		 number = firstAiredRightForm(strDate);
  	//Eftirskilyr�i: strDate er dagsetningu � forminu: yyyy-MM-dd'T'HH:mm:ss 
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
