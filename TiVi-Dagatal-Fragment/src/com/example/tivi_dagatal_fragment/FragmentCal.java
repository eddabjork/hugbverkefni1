/**
 * Nafn: 		Jï¿½hanna Agnes Magnï¿½sdï¿½ttir
 * Dagsetning: 	2. oktï¿½ber 2014
 * Markmiï¿½: 	Fragment sem sï¿½nir viku-dagatal sem inniheldur alla 
 * 				ï¿½ï¿½ ï¿½ï¿½tti sem eru aï¿½ birtast ï¿½ dagatalinu.
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
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
	
	//Notkun:		 setLayout();
  	//Eftirskilyrï¿½i: Bï¿½iï¿½ er aï¿½ setja upp grunnlag ï¿½tlits, sem er LinearLayout,
	//				 inn ï¿½ scrollView
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
	// Eftir:  takkar til aï¿½ flakka ï¿½ milli vikna hafa veriï¿½ settir ï¿½ btnLayout
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
	
	//Notkun:		 fillInDates(mainLayout);
  	//Eftirskilyrï¿½i: Bï¿½iï¿½ er aï¿½ bï¿½a til view fyrir sï¿½rhvern dag vikunnar (alls 7) ï¿½ar
    //				 sem fram kemur dagsetning og Layout-plï¿½ss fyrir dagatals-ï¿½ï¿½tti
    //				 ï¿½au eru svo sett inn ï¿½ mainLayout sem er ï¿½ scrollView.
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
	//Eftir:  Nafni viðeigandi mánaðar hefur verið bætt við mainLayout
	public void addNameOfMonth(Calendar cal, LinearLayout mainLayout){
		
		TextView month = new TextView(getActivity());
		month.setText(getMonthForInt(cal.get(Calendar.MONTH)));
		month.setPadding(10,10,10,10);
		month.setTextSize(20);
		mainLayout.addView(month);
		mainLayout.addView(makeLine());
	}
	
	//Notkun:		 setDateLayout(dayName, calendar, mainLayout);
  	//Eftirskilyrï¿½i: Bï¿½iï¿½ er aï¿½ bï¿½a til view fyrir einn dag ï¿½ar sem fram kemur
    //				 dagur vikunnar, dagur mï¿½naï¿½ars og mï¿½nuï¿½urinn sjï¿½lfur.
    //				 Einnig er bï¿½iï¿½ aï¿½ bï¿½a til LinearLayout inni ï¿½ ï¿½essu view sem er 
    //				 meï¿½ id (dagsetningin ï¿½ forminu yyMMdd) svo hï¿½gt sï¿½ aï¿½ bï¿½ta 
    //				 viï¿½ ï¿½ï¿½ttum ï¿½ rï¿½ttum staï¿½. ï¿½essu view er svo bï¿½tt viï¿½ neï¿½st ï¿½ 
	//				 mainLayout.
	public void setDateLayout(String dayName, Calendar cal, LinearLayout mainLayout) {		
		LinearLayout dayLayout = new LinearLayout(getActivity());
		dayLayout.setOrientation(LinearLayout.HORIZONTAL);
		dayLayout.setPadding(16,8,16,8);
		
		Context myContext = getActivity();
		WindowManager wm = (WindowManager) myContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int wd = (int) width/9;
		LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(wd, LinearLayout.LayoutParams.WRAP_CONTENT);
		
		LinearLayout dateLayout = new LinearLayout(getActivity());
		dateLayout.setLayoutParams(layoutParams);
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
  	//Eftirskilyrï¿½i: month er nafn mï¿½nuï¿½s miï¿½aï¿½ viï¿½ number ï¿½ar sem 
	//				 0=janï¿½ar,..,11=desember.
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
  	//Eftirskilyrï¿½i: line er nï¿½na view hlutur sem er einfï¿½ld, ï¿½unn, grï¿½ lï¿½na.
	public View makeLine(){
		 View v = new View(getActivity());
		 v.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 1, (float) 0.80));
		 v.setBackgroundColor(Color.rgb(203,203,203));
		 return v;
	 }
	
	/**
	 * Nafn: 		Kristï¿½n Fjï¿½la Tï¿½masdï¿½ttir
	 * Dagsetning: 	23. oktï¿½ber 2014
	 * Markmiï¿½: 	CalendarShowsTask framkvï¿½mir ï¿½rï¿½ï¿½avinnu sem nï¿½r ï¿½ alla ï¿½ï¿½tti 
	 * 				frï¿½ gagnagrunni sem eiga aï¿½ vera birtir ï¿½ dagatali
	 */
	private class CalendarShowsTask extends AsyncTask<Void, Integer, Map<String, String>> {
		// Notkun: map = doInBackground(voids)
		// Eftir:  map inniheldur gagna-titlana ï¿½ ï¿½eim ï¿½ï¿½ttum sem eiga aï¿½ vera
		//		   aï¿½ vera birtir ï¿½ dagatali
		protected Map<String, String> doInBackground(Void... voids) {
			DbUtils dbHelper = new DbUtils(getActivity());
	        Map<String, String> dataTitles = dbHelper.getOnCalShows();
			return dataTitles;
		}
		
		// Notkun: onPostExecute(dataTitles)
		// Eftir:  nï¿½r ï¿½rï¿½ï¿½ur byrjar sem nï¿½r ï¿½ upplï¿½singar um ï¿½ï¿½tti sem eiga 
		//		   aï¿½ vera birtir ï¿½ dagatali
		protected void onPostExecute(Map<String, String> dataTitles) {
			new CalendarEpisodesTask().execute(dataTitles);
		}
	}
	
	/**
	 * Nafn: 		Kristï¿½n Fjï¿½la Tï¿½masdï¿½ttir
	 * Dagsetning: 	23. oktï¿½ber 2014
	 * Markmiï¿½: 	CalendarEpisodesTask framkvï¿½mir ï¿½rï¿½ï¿½avinnu sem nï¿½r ï¿½ alla ï¿½ï¿½tti 
	 * 				frï¿½ vefï¿½jï¿½nustunni trakt.tv sem eiga aï¿½ vera birtir ï¿½ dagatali
	 */
	private class CalendarEpisodesTask extends AsyncTask<Map<String, String>, Integer, List<Episode>> {
		
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur veriï¿½ stillt sem ï¿½ aï¿½ sï¿½na ï¿½ meï¿½an notandi er aï¿½ bï¿½ï¿½a
		protected void onPreExecute() {  
    		progressDialog = LayoutUtils.showProgressDialog(R.string.list_process_title, 
    				R.string.list_process_msg, getActivity());	
        }  
		
		// Notkun: episodes = doInBackground(dataTitles)
		// Eftir:  episodes er listi af ï¿½ï¿½ttum sem ï¿½ aï¿½ birta ï¿½ dagatali
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
		// Eftir:  episodes hafa veriï¿½ birtir ï¿½ dagatali
		protected void onPostExecute(List<Episode> calendarEpisodes) {
			progressDialog.dismiss();
			for (Episode episode : calendarEpisodes){
				fillInEpisode(episode);
	        }
		}
	}
	
	//Notkun:		 fillInEpisode(episode);
  	//Eftirskilyrï¿½i: Bï¿½iï¿½ aï¿½ setja inn alla ï¿½ï¿½tti sem eru stilltir ï¿½ "ï¿½ dagatali"
	//				 ï¿½ rï¿½ttan staï¿½ (ï¿½.e. bï¿½ta viï¿½ ï¿½ view-iï¿½ sem hefur id-iï¿½
	//				 dagsetninguna ï¿½egar ï¿½ï¿½tturinn var frumsï¿½ndur)
	public void fillInEpisode(final Episode episode) {
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
	    textView.setPadding(20,0,0,0);
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
	    	Log.e("NÃ¡Ã°i ekki aÃ° setja Ã­ linear", "layout");
	    }
	}
	
	//Notkun:		 number = firstAiredRightForm(strDate);
  	//Eftirskilyrï¿½i: strDate er dagsetningu ï¿½ forminu: yyyy-MM-dd'T'HH:mm:ss 
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
	//Eftirskilyrï¿½i: Tï¿½minn ï¿½ deginum calendar hefur veriï¿½ settur ï¿½ 00:00:00
	public static Calendar nullifyTime(Calendar c){
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND,0);
		return c;
	}
	
	//Notkun: 		 date = getLastSundayForNumber(num)
	//Eftirskilyrï¿½i: date er sunnudagur eftir num-1 vikur
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
	//Eftirskilyrï¿½i: date er sunnudagur eftir num vikur
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