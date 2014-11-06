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
import java.util.List;
import java.util.Map;

import Clients.TraktClient;
import Data.DbUtils;
import Dtos.Episode;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

public class FragmentCal extends Fragment {
	private ScrollView scrollView;
	private ProgressDialog progressDialog;
	
	/** Sets the view */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cal, container, false);
		
		scrollView = new ScrollView(getActivity());
		setLayout();
		flushCash();
		new CalendarShowsTask().execute();

		view = scrollView;
        return view;
	}
	
	//Notkun:		 flushCash()
	//Eftirskilyrði: þáttum á dagatali hefur verið eytt úr cache-minni
	public void flushCash(){
		long time = System.currentTimeMillis();
		long twelveHours = (long) (60000*60*12);
		if((time - MainActivity.startTime) > twelveHours){
			MainActivity.cache.remove("calendarEpisodes");
			Log.v("cache", "Calendar episodes removed from cache");
		}
	}
	
	//Notkun:		 setLayout();
  	//Eftirskilyrði: Búið er að setja upp grunnlag útlits, sem er LinearLayout,
	//				 inn í scrollView
	public void setLayout(){
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    	
    	LinearLayout mainLayout = new LinearLayout(getActivity());
    	mainLayout.setOrientation(LinearLayout.VERTICAL);    	
    	fillInDates(mainLayout);

    	scrollView.addView(mainLayout);
	}
	
	//Notkun:		 fillInDates(mainLayout);
  	//Eftirskilyrði: Búið er að búa til view fyrir sérhvern dag vikunnar (alls 7) þar
    //				 sem fram kemur dagsetning og Layout-pláss fyrir dagatals-þætti
    //				 Þau eru svo sett inn í mainLayout sem er í scrollView.
	public void fillInDates(LinearLayout mainLayout){
		Calendar cal = Calendar.getInstance();
		
		String weekDayNames[] = {
				getResources().getString(R.string.sun_label),
				getResources().getString(R.string.mon_label),
				getResources().getString(R.string.tue_label),
				getResources().getString(R.string.wed_label),
				getResources().getString(R.string.thu_label),
				getResources().getString(R.string.fri_label),
				getResources().getString(R.string.sat_label)	
		};
		
		//Calendar.SUNDAY=1
		//Calendar.MONDAY=2
		
		if(Calendar.SUNDAY != 1) {
			String temp[] = new String[weekDayNames.length];
			for(int i=0; i<weekDayNames.length-1; i++){
				temp[i] = weekDayNames[i+1];
			}
			temp[weekDayNames.length-1] = weekDayNames[0];
			weekDayNames = temp;
		}

		for(int i=0; i<weekDayNames.length; i++){
			cal.set(Calendar.DAY_OF_WEEK, i+1);
			setDateLayout(weekDayNames[i], cal, mainLayout);
		}
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
		TextView dateMonth = new TextView(getActivity());
		dateMonth.setText(getMonthForInt(cal.get(Calendar.MONTH)));
		dateMonth.setGravity(Gravity.CENTER);
		dateMonth.setTextSize(10);
		dateLayout.addView(dateName);
		dateLayout.addView(dateDay);
		dateLayout.addView(dateMonth);
		
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
		        calendarEpisodes = trakt.getCalendarEpisodes(dataTitles[0]);
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
	public void fillInEpisode(Episode episode) {		
		String title = episode.getShowTitle() + ": " + episode.getTitle() + " (þáttur " + episode.getNumber() + ")";
		int episodeId = getFirstAiredInRightForm(episode.getFirstAired());
	
		LinearLayout linearLayout = (LinearLayout)getView().findViewById(episodeId);
		TextView textView = new TextView(getActivity());
	    textView.setText(title);
	    textView.setPadding(20,0,0,0);
	    textView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				//Missing
			}
		});
	    try {
	    	linearLayout.addView(textView);
	    } catch(Exception e) {
	    	
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
}