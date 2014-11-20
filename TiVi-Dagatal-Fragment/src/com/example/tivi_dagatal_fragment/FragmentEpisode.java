/**
 * Nafn: 		Steinunn Fri�geirsd�ttir
 * Dagsetning: 	30. okt�ber 2014
 * Markmi�: 	FragmentEpisopde er fragment sem birtir uppl�singar
 * 				um einstaka ��tti.
 */
package com.example.tivi_dagatal_fragment;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Clients.IMDbClient;
import Dtos.Episode;
import Dtos.Show;
import Threads.DownloadImageTask;
import Utils.LayoutUtils;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class FragmentEpisode extends Fragment{
	private ScrollView scrollView;
	private Episode episode;
	private ProgressDialog progressDialog;
	
	@Override
	//Eftir: B�i� a� birta uppl�singar um einstaka ��tt
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_episode, container, false);
		scrollView = new ScrollView(getActivity());
		new ShowEpisodeTask().execute(episode);
        return view;
	}
	
	//Notkun: setEpisode(episode)  
	//Eftir: b�i� er a� setja episode sem gildi tilviksbreytinnar episode
	public void setEpisode(Episode episode){
		this.episode = episode;
	}	
	
	/**
     * Nafn: 		Steinunn Fri�geirsd�ttir
     * Dagsetning: 	6. november 2014
     * Markmi�: 	ShowEpisodeTask er klasi sem s�r um �r��avinnslu fyrir FragmentEpisode.
     * 				Uppl�singum um ��tt er hla�i� inn asyncronus me� loading skj�.
     */   
	private class ShowEpisodeTask extends AsyncTask<Object, Integer, Episode> {
		//Notkun: doInBackground(queries)
		//Eftir:  ���avinnslu � bakgrunni er loki�
		//        og slila� hefur veri� r�ttum episode.
		protected Episode doInBackground(Object... objects) {         
			IMDbClient imdb = new IMDbClient();
			Episode episode = (Episode)objects[0];
			return episode;
		}
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur veri� stillt sem � a� s�na � me�an notandi er a� b��a
		protected void onPreExecute() {  
			progressDialog = LayoutUtils.showProgressDialog(R.string.popular_process_title, 
    				R.string.popular_process_msg, getActivity());	
        }  
		
		//Notkun: onPostExecute(searchShows)
		//Eftir:  b�i� a� er hla�a inn �llum uppl�singum um episode og birta 
		//		  �samt �v� a� stoppa progressDialog
		protected void onPostExecute(Episode episode) {
			progressDialog.dismiss();
								
			ImageView image = (ImageView) getView().findViewById(R.id.image);
			String imgUrl = episode.getScreen();
			new DownloadImageTask(image, getActivity()).execute(imgUrl);
			
			boolean bool = false;
			boolean bool2 = false;
			String text = null;

			TextView title = (TextView) getView().findViewById(R.id.title);
			bool = checkText(episode.getTitle());
			if(bool) text = episode.getTitle();
			else text = getResources().getString(R.string.title_missing);
			title.setText(text);

			DecimalFormat formatter = new DecimalFormat("00");
			String season = formatter.format(Integer.parseInt(episode.getSeason()));
			String number = formatter.format(Integer.parseInt(episode.getNumber()));
			TextView episodeNumber = (TextView) getView().findViewById(R.id.episodeNumber);
			bool = checkText(season);
			bool2 = checkText(number);
			if (bool && bool2) text = "s"+season+"e"+number;
			else text = getResources().getString(R.string.number_missing);
			episodeNumber.setText(text);
			
			Date date = null;
			TextView airTime = (TextView) getView().findViewById(R.id.airTime);
			try {
				date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(episode.getFirstAired());
				String newDate = new SimpleDateFormat("HH:mm").format(date);
				bool = checkText(newDate);
				if(bool) text = newDate;
				else text = getResources().getString(R.string.showtime_missing);
				airTime.setText(text);
			} catch (ParseException e) {
				e.printStackTrace();
				airTime.setText(getResources().getString(R.string.air_time_title) + " " + getResources().getString(R.string.showtime_missing));
			}
			
			TextView firstAired = (TextView) getView().findViewById(R.id.firstAired);
			try {
				date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(episode.getFirstAired());
				String newDate = new SimpleDateFormat("dd.MM.yyyy").format(date);
				String time = new SimpleDateFormat("HH:mm").format(date);
				bool = checkText(newDate);
				if(bool) text = newDate;
				else text = getResources().getString(R.string.date_missing);
				firstAired.setText(text);
			} catch (ParseException e) {
				e.printStackTrace();
				firstAired.setText(getResources().getString(R.string.first_aired_title)  + " " + getResources().getString(R.string.date_missing));
			}
			
			TextView plot = (TextView) getView().findViewById(R.id.plot);
			bool = checkText(episode.getOverview());
			if(bool) text = episode.getOverview();
			else text = getResources().getString(R.string.plot_title)  + "\n" + getResources().getString(R.string.desc_missing);
			plot.setText(text);  
		}
		
		protected boolean checkText (String string) {
			if(string == null || string.toString().isEmpty() || string.equals("TBA"))
				return false;
			return true;
		}
	}
	
	// Notkun: showDialog(show)
	// Eftir:  pop-up hefur veri� birt sem b��ur upp� a� vista show � dagatali 
	void showDialog(Show show) {
	    DialogFragment newFragment = PopUpPutOnCal.newInstance(show);
	    newFragment.show(getFragmentManager(), "dialog");
	}
}