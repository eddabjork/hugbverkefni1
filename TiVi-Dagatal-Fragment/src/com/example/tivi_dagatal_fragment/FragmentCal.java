/**
 * Nafn: 		Johanna Agnes Magnusdottir
 * Dagsetning: 	2. oktober 2014
 * Markmid: 	Fragment sem synir viku-dagatal sem inniheldur alla 
 * 				tha thaetti sem eiga ad birtast a dagatalinu.
 */

package com.example.tivi_dagatal_fragment;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import Clients.TraktClient;
import Data.DbUtils;
import Dtos.Episode;
import Utils.LayoutUtils;
import Utils.VariousUtils;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class FragmentCal extends Fragment {
	private ScrollView scrollView;
	private ProgressDialog progressDialog;
	private Fragment frag;
	private Date lastSunday = getLastFirstDayForNumber(MainActivity.getWeek());
	private Date nextSunday = getNextFirstDayForNumber(MainActivity.getWeek());
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	private String cacheKey = "calendarEpisodes";
	
	/** Sets the view */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cal, container, false);
		
		scrollView = new ScrollView(getActivity());
		setLayout();
		VariousUtils.flushCacheAfter12Hours(cacheKey);
		if(VariousUtils.isConnectedToInternet(getActivity())){
			new CalendarShowsTask().execute();
		} else {
			LayoutUtils.showNotConnectedMsg(getActivity());
		}
		
		view = scrollView;
        return view;
	}
	
	//Notkun: setLayout();
  	//Eftir: Buid er ad setja upp grunnlag utlits, sem er LinearLayout,
	//		 inn i scrollView
	public void setLayout(){
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    	
		LinearLayout scrollLayout = new LinearLayout(getActivity());
		scrollLayout.setOrientation(LinearLayout.VERTICAL);
		
		// layout for weekdays
    	LinearLayout mainLayout = new LinearLayout(getActivity());
    	mainLayout.setOrientation(LinearLayout.VERTICAL);    	
    	fillInDates(mainLayout);
    	scrollLayout.addView(mainLayout);
    	
    	// layout for week navigation buttons
    	RelativeLayout btnLayout = new RelativeLayout(getActivity());
    	addWeekNavButtons(btnLayout);
    	scrollLayout.addView(btnLayout);
    	
    	scrollView.addView(scrollLayout);
	}
	
	// Notkun: addWeekNavButtons(btnLayout)
	// Eftir:  Takkar til ad flakka a milli vikna hafa verid settir a btnLayout
	public void addWeekNavButtons(RelativeLayout btnLayout){		
		// go one week back
		ImageButton leftBtn = new ImageButton(getActivity());
		leftBtn.setImageResource(R.drawable.previous_week);
		leftBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	MainActivity.subtractWeek();
            	VariousUtils.flushCache(cacheKey);
            	FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                               .replace(R.id.content_frame, new FragmentCal())
                               .commit();
            }
        }); 
		leftBtn.setBackgroundColor(Color.TRANSPARENT);
		RelativeLayout.LayoutParams leftParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    	leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
    	btnLayout.addView(leftBtn, leftParams);
    	
    	// go to current week
    	ImageButton midBtn = new ImageButton(getActivity());
    	midBtn.setImageResource(R.drawable.today);
    	midBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	MainActivity.setCurrentWeek();
            	VariousUtils.flushCache(cacheKey);
            	FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                               .replace(R.id.content_frame, new FragmentCal())
                               .commit();
            }
        }); 
		midBtn.setBackgroundColor(Color.TRANSPARENT);
		RelativeLayout.LayoutParams midParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		midParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    	btnLayout.addView(midBtn, midParams);
    	
    	// go one week ahead
		ImageButton rightBtn = new ImageButton(getActivity());
    	rightBtn.setImageResource(R.drawable.next_week);
    	rightBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	MainActivity.addWeek();
            	VariousUtils.flushCache(cacheKey);
            	FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                               .replace(R.id.content_frame, new FragmentCal())
                               .commit();
            }
        });
    	rightBtn.setBackgroundColor(Color.TRANSPARENT);
    	RelativeLayout.LayoutParams rightParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    	rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    	btnLayout.addView(rightBtn, rightParams);
	}
	
	//Notkun: fillInDates(mainLayout);
  	//Eftir: Buid er ad bua til view fyrir serhvern dag vikunnar (alls 7) thar
    //		 sem fram kemur dagsetning og Layout-plass fyrir dagatals-thaetti
    //		 thau eru svo sett inn i mainLayout sem er i scrollView.
	public void fillInDates(LinearLayout mainLayout){
		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.WEEK_OF_YEAR, MainActivity.getWeek());
		int firstDay = cal.getFirstDayOfWeek();
		cal.add( Calendar.DAY_OF_WEEK, -(cal.get(Calendar.DAY_OF_WEEK)-firstDay));
		addNameOfMonth(cal, mainLayout);
		
		String weekDayNames[] = {
				getResources().getString(R.string.sun_label),
				getResources().getString(R.string.mon_label),
				getResources().getString(R.string.tue_label),
				getResources().getString(R.string.wed_label),
				getResources().getString(R.string.thu_label),
				getResources().getString(R.string.fri_label),
				getResources().getString(R.string.sat_label)	
		};
		
		String temp[] = new String[weekDayNames.length];
		if(cal.getFirstDayOfWeek() == 2){
			for(int i=0; i<temp.length-1;i++){
				temp[i]=weekDayNames[i+1];
			}
			temp[temp.length-1]=weekDayNames[0];
			weekDayNames=temp;
		}
		
		int lastDayOfMonth = Integer.MIN_VALUE;
		for(int i=0; i < weekDayNames.length; i++){
			int thisDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
			if(lastDayOfMonth > thisDayOfMonth){
				addNameOfMonth(cal, mainLayout);
			}
			setDateLayout(weekDayNames[i], cal, mainLayout);
			lastDayOfMonth = thisDayOfMonth;
			cal.add(Calendar.DATE, 1);
		}
	}
	
	//Notkun: addNameOfMont(cal, mainLayout)
	//Eftir:  Nafni videigandi manadar hefur verid baett vid mainLayout
	public void addNameOfMonth(Calendar cal, LinearLayout mainLayout){
		int pd = VariousUtils.getScreenWidth(getActivity());
		TextView month = new TextView(getActivity());
		month.setText(getMonthForInt(cal.get(Calendar.MONTH)));
		month.setPadding(pd/32,pd/32,pd/32,pd/32);
		month.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		mainLayout.addView(month);
		mainLayout.addView(LayoutUtils.makeLine(getActivity()));
	}
	
	//Notkun: setDateLayout(dayName, calendar, mainLayout);
  	//Eftir: Buid er ad bu til view fyrir einn dag thar sem fram kemur
    //		 dagur vikunnar, dagur manadars og manudurinn sjalfur.
    //		 Einnig er buid ad bua til LinearLayout inni i thessu view sem er 
    //		 med id (dagsetningin a forminu yyMMdd) svo haegt se ad birta 
    //		 tilsvarandi thattum a rettan stad a dagatali. thessu view er svo 
	//		 baett vid nedst i mainLayout.
	public void setDateLayout(String dayName, Calendar cal, LinearLayout mainLayout) {
		int pd = VariousUtils.getScreenWidth(getActivity());
		LinearLayout dayLayout = new LinearLayout(getActivity());
		dayLayout.setOrientation(LinearLayout.HORIZONTAL);
		dayLayout.setPadding(pd/20,pd/40,pd/20,pd/40);
		
		LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(pd/9, LinearLayout.LayoutParams.WRAP_CONTENT);
		
		LinearLayout dateLayout = new LinearLayout(getActivity());
		dateLayout.setLayoutParams(layoutParams);
		dateLayout.setOrientation(LinearLayout.VERTICAL);
		TextView dateName = new TextView(getActivity());
		dateName.setText(dayName);
		dateName.setGravity(Gravity.CENTER);
		TextView dateDay = new TextView(getActivity());
		dateDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
		dateDay.setGravity(Gravity.CENTER);
		dateDay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		dateLayout.addView(dateName);
		dateLayout.addView(dateDay);
		
		LinearLayout episodesLayout = new LinearLayout(getActivity());
		episodesLayout.setOrientation(LinearLayout.VERTICAL);
		String date = new SimpleDateFormat("yyMMdd").format(cal.getTime());
		episodesLayout.setId(Integer.parseInt(date));
		
		dayLayout.addView(dateLayout);
		dayLayout.addView(episodesLayout);
		
		mainLayout.addView(dayLayout);
		mainLayout.addView(LayoutUtils.makeLine(getActivity()));
	}
	
	//Notkun: month = getMonthFromInt(number);
  	//Eftir: month er nafn manadars midad vid number thar sem 
	//		 0=januar,..,11=desember.
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
	
	/**
	 * Nafn: 		Kristin Fjola Tomasdottir
	 * Dagsetning: 	23. oktober 2014
	 * Markmid: 	CalendarShowsTask framkvaemir thradaavinnu sem naer i alla thaetti 
	 * 				fra gagnagrunni sem eiga ad vera birtir a dagatali
	 */
	private class CalendarShowsTask extends AsyncTask<Void, Integer, Map<String, String>> {
		// Notkun: map = doInBackground(voids)
		// Eftir:  map inniheldur gagna-titlana a theim thattum sem eiga ad vera
		//		   birtir a dagatali
		protected Map<String, String> doInBackground(Void... voids) {
			DbUtils dbHelper = new DbUtils(getActivity());
	        Map<String, String> dataTitles = dbHelper.getOnCalShows();
			return dataTitles;
		}
		
		// Notkun: onPostExecute(dataTitles)
		// Eftir:  Nyr thradur byrjar sem naer i upplysingar um thaetti sem eiga 
		//		   ad vera birtir a dagatali
		protected void onPostExecute(Map<String, String> dataTitles) {
			new CalendarEpisodesTask().execute(dataTitles);
		}
	}
	
	/**
	 * Nafn: 		Kristin Fjola Tomasdottir
	 * Dagsetning: 	23. oktober 2014
	 * Markmid: 	CalendarEpisodesTask framkvaemir thradaavinnu sem naer i alla thaetti 
	 * 				fra vefthjonustunni trakt.tv sem eiga ad vera birtir a dagatali
	 */
	private class CalendarEpisodesTask extends AsyncTask<Map<String, String>, Integer, List<Episode>> {
		
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur verid stillt sem a ad syna a medan notandi er ad bida
		protected void onPreExecute() {  
    		progressDialog = LayoutUtils.showProgressDialog(R.string.list_process_title, 
    				R.string.list_process_msg, getActivity());	
        }  
		
		// Notkun: episodes = doInBackground(dataTitles)
		// Eftir:  episodes er listi af thattum sem a ad birta a dagatali
		protected List<Episode> doInBackground(Map<String, String>... dataTitles) {
			List<Episode> calendarEpisodes = (List<Episode>) MainActivity.getCache().get(cacheKey);
	        
	        if(calendarEpisodes == null || calendarEpisodes.size() == 0) {
		    	Log.v("cache", "Calendar episodes not cached, retrieving new list");
		    	TraktClient trakt = new TraktClient();
		        calendarEpisodes = trakt.getCalendarEpisodes(dataTitles[0], lastSunday, nextSunday);
		    	MainActivity.getCache().put(cacheKey, calendarEpisodes);
		    } else {
		    	Log.v("cahce", "Cached episodes found");
		    	Log.v("cache", "Cache response size: " + calendarEpisodes.size());
		    }
	        
			return calendarEpisodes;
		}
		
		// Notkun: onPostExecute(episodes)
		// Eftir:  episodes hafa verid birtir a dagatali
		protected void onPostExecute(List<Episode> calendarEpisodes) {
			progressDialog.dismiss();
			for (Episode episode : calendarEpisodes){
				fillInEpisode(episode);
	        }
		}
	}
	
	//Notkun: fillInEpisode(episode);
  	//Eftir: Buid er ad setja inn alla thaetti sem eru stilltir a "a dagatali"
	//		 a rettan stad (th.e. baeta vid i view-id sem hefur id-id
	//		 dagsetninguna thegar thatturinn var frumsyndur)
	public void fillInEpisode(final Episode episode) {
		int pd = VariousUtils.getScreenWidth(getActivity());
		
		frag = new FragmentEpisode();
		
		DecimalFormat formatter = new DecimalFormat("00");
		String season = formatter.format(Integer.parseInt(episode.getSeason()));
		String number = formatter.format(Integer.parseInt(episode.getNumber()));
		String episodeNumber = "s"+season+"e"+number;
		
		String text = null;
		if(!episode.getTitle().equals("TBA")) text = episode.getTitle();
		else text = "Vantar titil";
		
		String title = episode.getShowTitle() + ": " + text + " (" + episodeNumber + ")";
		int episodeId = getFirstAiredInRightForm(episode.getFirstAired());
	
		LinearLayout linearLayout = (LinearLayout)getView().findViewById(episodeId);
		TextView textView = new TextView(getActivity());
	    textView.setText(title);
	    textView.setPadding(pd/16,0,0,0);
	    textView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				((FragmentEpisode) frag).setEpisode(episode);
				FragmentManager fragmentManager = getFragmentManager();
				VariousUtils.addFragmentToStack(fragmentManager, frag);
		        getActivity().getActionBar().setTitle(episode.getShowTitle());
		        
			}
		});
	    try {
	    	linearLayout.addView(textView);
	    } catch(Exception e) {
	    	Log.e("Náði ekki að setja í linear", "layout");
	    }
	}
	
	//Notkun: number = firstAiredRightForm(strDate);
  	//Eftir: strDate er dagsetningu a forminu: yyyy-MM-dd'T'HH:mm:ss 
	//		 number er talan yyMMdd.
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
	
	//Notkun: calendar = nullifyTime(c)
	//Eftir: Timinn a deginum calendar hefur verid settur a 00:00:00
	public static Calendar nullifyTime(Calendar c){
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND,0);
		return c;
	}
	
	//Notkun: date = getLastSundayForNumber(num)
	//Eftir: date er sunnudagur eftir num-1 vikur
	public static Date getLastFirstDayForNumber(int num){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		int day = Calendar.SUNDAY;
		if(c.getFirstDayOfWeek() == 2){ day = Calendar.MONDAY;}
		c.set(Calendar.DAY_OF_WEEK, day);
		c.add(Calendar.WEEK_OF_YEAR, num);
		c = nullifyTime(c);
		Date lastSunday = c.getTime();
		format.format(lastSunday);
		
		return lastSunday;
	}
	
	//Notkun: date = getNextSundayForNumber(num)
	//Eftir: date er sunnudagur eftir num vikur
	public static Date getNextFirstDayForNumber(int num){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		int firstDayOfWeek = c.getFirstDayOfWeek();
		c.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
		c.add(Calendar.DATE, 7);
		c.add(Calendar.WEEK_OF_YEAR, num);
		c = nullifyTime(c);
		Date nextSunday = c.getTime();
		format.format(nextSunday);
		
		return nextSunday;
	}
}