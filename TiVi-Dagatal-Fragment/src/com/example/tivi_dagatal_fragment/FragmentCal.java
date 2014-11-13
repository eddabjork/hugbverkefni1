/**
 * Nafn: 		Jóhanna Agnes Magnúsdóttir
 * Dagsetning: 	2. október 2014
 * Markmið: 	Fragment sem sýnir viku-dagatal sem inniheldur alla 
 * 				þá þætti sem eru að birtast á dagatalinu.
 */

package com.example.tivi_dagatal_fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import Clients.TraktClient;
import Data.DbUtils;
import Dtos.Episode;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

public class FragmentCal extends Fragment {
	private ScrollView scrollView;
	private ProgressDialog progressDialog;
	private Fragment frag;
	private Date lastSunday = getLastFirstDayForNumber(MainActivity.getWeek());
	private Date nextSunday = getNextFirstDayForNumber(MainActivity.getWeek());
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
	/** Sets the view */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cal, container, false);
		
		scrollView = new ScrollView(getActivity());
		setLayout();
		flushCache();
		new CalendarShowsTask().execute();

		view = scrollView;
        return view;
	}
	
	//Notkun:		 flushCache()
	//Eftirskilyrði: þáttum á dagatali hefur verið eytt úr cache-minni
	public static void flushCache(){
		long time = System.currentTimeMillis();
		long twelveHours = (long) (60000*60*12);
		if((time - MainActivity.startTime) > twelveHours){
			flushCacheNow();
		}
	}
	
	//Notkun:		 flushCacheNow()
	//Eftirskilyrði: þáttum á dagatali hefur verið eytt úr cache-minni
	public static void flushCacheNow(){
		MainActivity.cache.remove("calendarEpisodes");
		Log.v("cache", "Calendar episodes removed from cache");
	}
	
	//Notkun:		 setLayout();
  	//Eftirskilyrði: Búið er að setja upp grunnlag útlits, sem er LinearLayout,
	//				 inn í scrollView
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
	// Eftir:  takkar til að flakka á milli vikna hafa verið settir í btnLayout
	public void addWeekNavButtons(RelativeLayout btnLayout){		
		// go one week back
		ImageButton leftBtn = new ImageButton(getActivity());
		leftBtn.setImageResource(R.drawable.previous_week);
		leftBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	MainActivity.subtractWeek();
            	flushCacheNow();
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
    	midBtn.setImageResource(R.drawable.ic_launcher);
    	midBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	MainActivity.setCurrentWeek();
            	flushCacheNow();
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
            	flushCacheNow();
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
	
	//Notkun:		 fillInDates(mainLayout);
  	//Eftirskilyrði: Búið er að búa til view fyrir sérhvern dag vikunnar (alls 7) þar
    //				 sem fram kemur dagsetning og Layout-pláss fyrir dagatals-þætti
    //				 Þau eru svo sett inn í mainLayout sem er í scrollView.
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
	
	public void addNameOfMonth(Calendar cal, LinearLayout mainLayout){
		TextView month = new TextView(getActivity());
		month.setText(getMonthForInt(cal.get(Calendar.MONTH)));
		month.setPadding(10,10,10,10);
		month.setTextSize(20);
		mainLayout.addView(month);
		mainLayout.addView(makeLine());
	}
	
	//Notkun:		 setDateLayout(dayName, calendar, mainLayout);
  	//Eftirskilyrði: Búið er að búa til view fyrir einn dag þar sem fram kemur
    //				 dagur vikunnar, dagur mánaðars og mánuðurinn sjálfur.
    //				 Einnig er búið að búa til LinearLayout inni í þessu view sem er 
    //				 með id (dagsetningin á forminu yyMMdd) svo hægt sé að bæta 
    //				 við þættum á réttum stað. Þessu view er svo bætt við neðst í 
	//				 mainLayout.
	public void setDateLayout(String dayName, Calendar cal, LinearLayout mainLayout) {		
		LinearLayout dayLayout = new LinearLayout(getActivity());
		dayLayout.setOrientation(LinearLayout.HORIZONTAL);
		dayLayout.setPadding(16,8,16,8);
		
		LinearLayout dateLayout = new LinearLayout(getActivity());
		dateLayout.setOrientation(LinearLayout.VERTICAL);
		TextView dateName = new TextView(getActivity());
		dateName.setText(dayName);
		dateName.setGravity(Gravity.CENTER);
		TextView dateDay = new TextView(getActivity());
		dateDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
		dateDay.setGravity(Gravity.CENTER);
		dateDay.setTextSize(20);
		dateLayout.addView(dateName);
		dateLayout.addView(dateDay);
		
		LinearLayout episodesLayout = new LinearLayout(getActivity());
		episodesLayout.setOrientation(LinearLayout.VERTICAL);
		String date = new SimpleDateFormat("yyMMdd").format(cal.getTime());
		episodesLayout.setId(Integer.parseInt(date));
		
		dayLayout.addView(dateLayout);
		dayLayout.addView(episodesLayout);
		
		mainLayout.addView(dayLayout);
		mainLayout.addView(makeLine());
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
	
	//Notkun:		 line = makeLine();
  	//Eftirskilyrði: line er núna view hlutur sem er einföld, þunn, grá lína.
	public View makeLine(){
		 View v = new View(getActivity());
		 v.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 1, (float) 0.80));
		 v.setBackgroundColor(Color.rgb(203,203,203));
		 return v;
	 }
	
	/**
	 * Nafn: 		Kristín Fjóla Tómasdóttir
	 * Dagsetning: 	23. október 2014
	 * Markmið: 	CalendarShowsTask framkvæmir þráðavinnu sem nær í alla þætti 
	 * 				frá gagnagrunni sem eiga að vera birtir á dagatali
	 */
	private class CalendarShowsTask extends AsyncTask<Void, Integer, Map<String, String>> {
		// Notkun: map = doInBackground(voids)
		// Eftir:  map inniheldur gagna-titlana á þeim þáttum sem eiga að vera
		//		   að vera birtir á dagatali
		protected Map<String, String> doInBackground(Void... voids) {
			DbUtils dbHelper = new DbUtils(getActivity());
	        Map<String, String> dataTitles = dbHelper.getOnCalShows();
			return dataTitles;
		}
		
		// Notkun: onPostExecute(dataTitles)
		// Eftir:  nýr þráður byrjar sem nær í upplýsingar um þætti sem eiga 
		//		   að vera birtir á dagatali
		protected void onPostExecute(Map<String, String> dataTitles) {
			new CalendarEpisodesTask().execute(dataTitles);
		}
	}
	
	/**
	 * Nafn: 		Kristín Fjóla Tómasdóttir
	 * Dagsetning: 	23. október 2014
	 * Markmið: 	CalendarEpisodesTask framkvæmir þráðavinnu sem nær í alla þætti 
	 * 				frá vefþjónustunni trakt.tv sem eiga að vera birtir á dagatali
	 */
	private class CalendarEpisodesTask extends AsyncTask<Map<String, String>, Integer, List<Episode>> {
		
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur verið stillt sem á að sýna á meðan notandi er að bíða
		protected void onPreExecute() {  
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(getResources().getString(R.string.list_process_title));  
            progressDialog.setMessage(getResources().getString(R.string.list_process_msg)); 
            progressDialog.setCancelable(false);  
            progressDialog.setIndeterminate(false);  
            progressDialog.show();  
        }  
		
		// Notkun: episodes = doInBackground(dataTitles)
		// Eftir:  episodes er listi af þáttum sem á að birta á dagatali
		protected List<Episode> doInBackground(Map<String, String>... dataTitles) {
			List<Episode> calendarEpisodes = (List<Episode>) MainActivity.cache.get("calendarEpisodes");
	        
	        if(calendarEpisodes == null || calendarEpisodes.size() == 0) {
		    	Log.v("cache", "Calendar episodes not cached, retrieving new list");
		    	TraktClient trakt = new TraktClient();
		        calendarEpisodes = trakt.getCalendarEpisodes(dataTitles[0], lastSunday, nextSunday);
		        for(Episode episode : calendarEpisodes){
		        	Log.v("episode", episode.getDataTitle());
		        }
		    	MainActivity.cache.put("calendarEpisodes", calendarEpisodes);
		    } else {
		    	Log.v("cahce", "Cached episodes found");
		    	Log.v("cache", "Cache response size: " + calendarEpisodes.size());
		    }
	        
			return calendarEpisodes;
		}
		
		// Notkun: onPostExecute(episodes)
		// Eftir:  episodes hafa verið birtir á dagatali
		protected void onPostExecute(List<Episode> calendarEpisodes) {
			progressDialog.dismiss();
			for (Episode episode : calendarEpisodes){
				fillInEpisode(episode);
	        }
		}
	}
	
	//Notkun:		 fillInEpisode(episode);
  	//Eftirskilyrði: Búið að setja inn alla þætti sem eru stilltir á "á dagatali"
	//				 á réttan stað (þ.e. bæta við í view-ið sem hefur id-ið
	//				 dagsetninguna þegar þátturinn var frumsýndur)
	public void fillInEpisode(final Episode episode) {
		frag = new FragmentEpisode();
		
		String title = episode.getShowTitle() + ": " + episode.getTitle() + " (þáttur " + episode.getNumber() + ")"; //TODO: færa í string.xml!
		int episodeId = getFirstAiredInRightForm(episode.getFirstAired());
	
		LinearLayout linearLayout = (LinearLayout)getView().findViewById(episodeId);
		TextView textView = new TextView(getActivity());
	    textView.setText(title);
	    textView.setPadding(20,0,0,0);
	    textView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				((FragmentEpisode) frag).setEpisode(episode);
				FragmentManager fragmentManager = getFragmentManager();
		        fragmentManager.beginTransaction()
		                       .replace(R.id.content_frame, frag)
		                       .commit();
		        getActivity().getActionBar().setTitle(episode.getShowTitle());
		        
			}
		});
	    try {
	    	linearLayout.addView(textView);
	    } catch(Exception e) {
	    	Log.v("Náði ekki að setja í linear", "layout");
	    }
	}
	
	//Notkun:		 number = firstAiredRightForm(strDate);
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
	
	//Notkun: 		 calendar = nullifyTime(c)
	//Eftirskilyrði: Tíminn á deginum calendar hefur verið settur á 00:00:00
	public static Calendar nullifyTime(Calendar c){
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND,0);
		return c;
	}
	
	//Notkun: 		 date = getLastSundayForNumber(num)
	//Eftirskilyrði: date er sunnudagur eftir num-1 vikur
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
	
	//Notkun: 		 date = getNextSundayForNumber(num)
	//Eftirskilyrði: date er sunnudagur eftir num vikur
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