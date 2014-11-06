/**
 * Nafn: 		J�hanna Agnes Magn�sd�ttir
 * Dagsetning: 	2. okt�ber 2014
 * Markmi�: 	Fragment sem s�nir viku-dagatal sem inniheldur alla 
 * 				�� ��tti sem eru a� birtast � dagatalinu.
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
	//Eftirskilyr�i: ��ttum � dagatali hefur veri� eytt �r cache-minni
	public void flushCash(){
		long time = System.currentTimeMillis();
		long twelveHours = (long) (60000*60*12);
		if((time - MainActivity.startTime) > twelveHours){
			MainActivity.cache.remove("calendarEpisodes");
			Log.v("cache", "Calendar episodes removed from cache");
		}
	}
	
	//Notkun:		 setLayout();
  	//Eftirskilyr�i: B�i� er a� setja upp grunnlag �tlits, sem er LinearLayout,
	//				 inn � scrollView
	public void setLayout(){
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    	
    	LinearLayout mainLayout = new LinearLayout(getActivity());
    	mainLayout.setOrientation(LinearLayout.VERTICAL);    	
    	fillInDates(mainLayout);

    	scrollView.addView(mainLayout);
	}
	
	//Notkun:		 fillInDates(mainLayout);
  	//Eftirskilyr�i: B�i� er a� b�a til view fyrir s�rhvern dag vikunnar (alls 7) �ar
    //				 sem fram kemur dagsetning og Layout-pl�ss fyrir dagatals-��tti
    //				 �au eru svo sett inn � mainLayout sem er � scrollView.
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
  	//Eftirskilyr�i: B�i� er a� b�a til view fyrir einn dag �ar sem fram kemur
    //				 dagur vikunnar, dagur m�na�ars og m�nu�urinn sj�lfur.
    //				 Einnig er b�i� a� b�a til LinearLayout inni � �essu view sem er 
    //				 me� id (dagsetningin � forminu yyMMdd) svo h�gt s� a� b�ta 
    //				 vi� ��ttum � r�ttum sta�. �essu view er svo b�tt vi� ne�st � 
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
	
	//Notkun:		 line = makeLine();
  	//Eftirskilyr�i: line er n�na view hlutur sem er einf�ld, �unn, gr� l�na.
	public View makeLine(){
		 View v = new View(getActivity());
		 v.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 1, (float) 0.80));
		 v.setBackgroundColor(Color.rgb(203,203,203));
		 return v;
	 }
	
	/**
	 * Nafn: 		Krist�n Fj�la T�masd�ttir
	 * Dagsetning: 	23. okt�ber 2014
	 * Markmi�: 	CalendarShowsTask framkv�mir �r��avinnu sem n�r � alla ��tti 
	 * 				fr� gagnagrunni sem eiga a� vera birtir � dagatali
	 */
	private class CalendarShowsTask extends AsyncTask<Void, Integer, Map<String, String>> {
		// Notkun: map = doInBackground(voids)
		// Eftir:  map inniheldur gagna-titlana � �eim ��ttum sem eiga a� vera
		//		   a� vera birtir � dagatali
		protected Map<String, String> doInBackground(Void... voids) {
			DbUtils dbHelper = new DbUtils(getActivity());
	        Map<String, String> dataTitles = dbHelper.getOnCalShows();
			return dataTitles;
		}
		
		// Notkun: onPostExecute(dataTitles)
		// Eftir:  n�r �r��ur byrjar sem n�r � uppl�singar um ��tti sem eiga 
		//		   a� vera birtir � dagatali
		protected void onPostExecute(Map<String, String> dataTitles) {
			new CalendarEpisodesTask().execute(dataTitles);
		}
	}
	
	/**
	 * Nafn: 		Krist�n Fj�la T�masd�ttir
	 * Dagsetning: 	23. okt�ber 2014
	 * Markmi�: 	CalendarEpisodesTask framkv�mir �r��avinnu sem n�r � alla ��tti 
	 * 				fr� vef�j�nustunni trakt.tv sem eiga a� vera birtir � dagatali
	 */
	private class CalendarEpisodesTask extends AsyncTask<Map<String, String>, Integer, List<Episode>> {
		
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur veri� stillt sem � a� s�na � me�an notandi er a� b��a
		protected void onPreExecute() {  
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(getResources().getString(R.string.list_process_title));  
            progressDialog.setMessage(getResources().getString(R.string.list_process_msg)); 
            progressDialog.setCancelable(false);  
            progressDialog.setIndeterminate(false);  
            progressDialog.show();  
        }  
		
		// Notkun: episodes = doInBackground(dataTitles)
		// Eftir:  episodes er listi af ��ttum sem � a� birta � dagatali
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
		// Eftir:  episodes hafa veri� birtir � dagatali
		protected void onPostExecute(List<Episode> calendarEpisodes) {
			progressDialog.dismiss();  
			for (Episode episode : calendarEpisodes){
	        	fillInEpisode(episode);
	        }
		}
	}
	
	//Notkun:		 fillInEpisode(episode);
  	//Eftirskilyr�i: B�i� a� setja inn alla ��tti sem eru stilltir � "� dagatali"
	//				 � r�ttan sta� (�.e. b�ta vi� � view-i� sem hefur id-i�
	//				 dagsetninguna �egar ��tturinn var frums�ndur)
	public void fillInEpisode(Episode episode) {		
		String title = episode.getShowTitle() + ": " + episode.getTitle() + " (��ttur " + episode.getNumber() + ")";
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
}