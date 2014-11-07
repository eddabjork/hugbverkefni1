/**
 * Nafn: 		Steinunn Friðgeirsdóttir
 * Dagsetning: 	30. október 2014
 * Markmið: 	FragmentEpisopde er fragment sem birtir upplýsingar
 * 				um einstaka þætti.
 */
package com.example.tivi_dagatal_fragment;

import java.util.List;

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
import android.widget.Button;
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
		view = scrollView;
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
	private class ShowEpisodeTask extends AsyncTask<String, Integer, Episode> {
		//Notkun: doInBackground(queries)
		//Eftir:  þáðavinnslu í bakgrunni er lokið
		//        Í þráðavinnslu hér er kallað á vefþjónustuna
		//		  og cache búið til eða sótt.
		protected Episode doInBackground(String... queries) {         
			IMDbClient imdb = new IMDbClient();
			return new Episode();
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
		//Eftir:  Búið er að taka serchShows listann og
		//        birta þá ásamt takka til þess að bæta við 
		//        þáttaröð á dagatal. Listinn er svo birtur.
		protected void onPostExecute(List<Show> searchShows) {
			progressDialog.dismiss();
			LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT,
			LayoutParams.MATCH_PARENT);
			LinearLayout llv = new LinearLayout(getActivity());
			llv.setOrientation(LinearLayout.VERTICAL);
			for (final Show show : searchShows){
				TextView textView = new TextView(getActivity());
				textView.setText(show.getTitle());
				textView.setLayoutParams(lparams);
				Button button = new Button(getActivity());
				button.setText(getResources().getString(R.string.search_add));
				button.setLayoutParams(lparams);
				//Bï¿½ta titli og takka ï¿½ linearlayout
				llv.addView(textView);
				llv.addView(button);
			}
			//Bï¿½ta linearlayoutinu ï¿½ scrollview
			scrollView.addView(llv);
			//Birta nï¿½ja viewiï¿½
			//setContentView(sv);
		}
	}
	
	// Notkun: showDialog(show)
	// Eftir:  pop-up hefur verið birt sem býður uppá að vista show á dagatali 
	void showDialog(Show show) {
	    DialogFragment newFragment = PutOnCalPopUp.newInstance(R.string.popup_put_cal, show);
	    newFragment.show(getFragmentManager(), "dialog");
	}
}	

