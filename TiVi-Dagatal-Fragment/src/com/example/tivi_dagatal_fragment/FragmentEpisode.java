/**
 * Nafn: 		Steinunn Friðgeirsdóttir
 * Dagsetning: 	30. október 2014
 * Markmið: 	FragmentEpisopde er fragment sem birtir upplýsingar
 * 				um einstaka þætti.
 */
package com.example.tivi_dagatal_fragment;

import Clients.IMDbClient;
import Dtos.Episode;
import Dtos.Show;
import android.app.ActionBar.LayoutParams;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class FragmentEpisode extends Fragment{
	private ScrollView scrollView;
	private Episode episode;
	private ProgressDialog progressDialog;
	
	@Override
	//Eftir: Búið að birta upplýsingar um einstaka þátt
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_episode, container, false);
		scrollView = new ScrollView(getActivity());
		new ShowEpisodeTask().execute(episode);
		//view = scrollView;
		Log.v("er að búa til ", "episode");
        return view;
	}
	
	
	public void setEpisode(Episode episode){
		this.episode = episode;
		Log.v("Var að sækja episode", episode.getTitle());
	}	
	
	/**
     * Nafn: 		Steinunn Friðgeirsdóttir
     * Dagsetning: 	6. november 2014
     * Markmið: 	?????????????????????????????????????????????????????????????????????????????????????
     */   
	private class ShowEpisodeTask extends AsyncTask<Object, Integer, Episode> {
		//Notkun: doInBackground(queries)
		//Eftir:  þáðavinnslu í bakgrunni er lokið
		//        ????????????????????????????????????????????????????????????????????????????????????????
		protected Episode doInBackground(Object... objects) {         
			IMDbClient imdb = new IMDbClient();
			Episode episode = (Episode)objects[0];
			return episode;
		}
		
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur verið stillt sem á að sýna á meðan notandi er að bíða
		protected void onPreExecute() {  
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(getResources().getString(R.string.process_title_popular));  
            progressDialog.setMessage(getResources().getString(R.string.process_msg_popular)); 
            progressDialog.setCancelable(false);  
            progressDialog.setIndeterminate(false);  
            progressDialog.show();  
        }  
		
		//Engin virkni
		protected void onProgressUpdate(Integer... progress) {
			//setProgressPercent(progress[0]);
		}
		
		//Notkun: onPostExecute(searchShows)
		//Eftir:  ????????????????????????????????????????????????????????????????
		protected void onPostExecute(Episode episode) {
			progressDialog.dismiss();
			/*LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT,
			LayoutParams.MATCH_PARENT);
			LinearLayout llv = new LinearLayout(getActivity());
			llv.setOrientation(LinearLayout.VERTICAL);
			TextView textView = new TextView(getActivity());
			textView.setText("Titill þáttarins er " + episode.getTitle());
			llv.addView(textView);
			//Bï¿½ta linearlayoutinu ï¿½ scrollview
			scrollView.addView(llv);*/
			//Birta nï¿½ja viewiï¿½
			//setContentView(sv);
			
			//TODO: Bretya öllu í xml strengi
			LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			TextView title = (TextView) getView().findViewById(R.id.title);
			title.setText("Titill þáttar: " + episode.getTitle());
			
			TextView rating = (TextView) getView().findViewById(R.id.rating);
			rating.setText("Einkunn: " + episode.getImdbId()); //TODO: TEMP
			
			TextView season = (TextView) getView().findViewById(R.id.season);
			season.setText("Sería: " + episode.getSeason()); 
			
			TextView number = (TextView) getView().findViewById(R.id.number);
			number.setText("Þáttur: " + episode.getNumber());
			
			TextView firstAired = (TextView) getView().findViewById(R.id.firstAired);
			firstAired.setText("Fyrst sýndur: " + episode.getFirstAired()); //TODO: setja upp á eðlilegt form
			
			TextView overview = (TextView) getView().findViewById(R.id.overview);
			overview.setText("Um söguþráð þáttar: " + episode.getOverview()); 
		}
		
		public TextView addTextView(String text){
			TextView textView = new TextView(getActivity());
			textView.setText(text);
			return textView;
		}
	}
	
	// Notkun: showDialog(show)
	// Eftir:  pop-up hefur verið birt sem býður uppá að vista show á dagatali 
	void showDialog(Show show) {
	    DialogFragment newFragment = PutOnCalPopUp.newInstance(R.string.popup_put_cal, show);
	    newFragment.show(getFragmentManager(), "dialog");
	}
}	

