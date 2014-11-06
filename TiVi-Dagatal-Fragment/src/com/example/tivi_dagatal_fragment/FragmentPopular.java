/**
 * Nafn: 		Steinunn Friðgeirsdóttir
 * Dagsetning: 	30. október 2014
 * Markmið: 	FragmentPopular er fragment sem birtir lista
 * 				af visælum þáttum
 */
package com.example.tivi_dagatal_fragment;

import java.util.List;

import Clients.TraktClient;
import Data.DbUtils;
import Dtos.Episode;
import Dtos.Show;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
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

public class FragmentPopular extends Fragment {
	private DbUtils dbHelper;
	private ScrollView scrollView;
	private ProgressDialog progressDialog;
	
	@Override
	//Eftir: Birtir fragmentið sem sýnir vinsæla þætti
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_popular, container, false);
		flushCash();
		scrollView = new ScrollView(getActivity());
		new PopularShowsTask().execute();
		rootView = scrollView;
		
        return rootView;
    }
	
	// Notkun: flushCash()
	// Eftir:  vinsælum þáttum hafa verið eytt úr cache-minni
	public void flushCash(){
		long time = System.currentTimeMillis();
		long twelveHours = (long) (60000*60*12);
		if((time - MainActivity.startTime) > twelveHours){
			MainActivity.cache.remove("popularShows");
			Log.v("cache", "Popular shows removed from cache");
		}
	}
	
	//Notkun: onAttach(activity)
	//Eftir: Búið að tengja gagnagrunn við fragmentið
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        dbHelper = new DbUtils(activity);
    }

	/**
     * Nafn: 		Steinunn Friðgeirsdóttir
     * Dagsetning: 	30. október 2014
     * Markmið: 	Framkvæmir þráðavinnu til að birta vinsæla þætti 
     * 				frá vefþjónustu í fragmenti með loadi.
     * 				Clasinn geymir einnig cache fyrir þættina svo
     * 				það þurfi ekki að sækja alla þættina oft.
     */   
	private class PopularShowsTask extends AsyncTask<String, Integer, List<Show>> {
		//Notkun: doInBackground(queries)
		//Eftir:  þáðavinnslu í bakgrunni er lokið
		//        Í þráðavinnslu hér er kallað á vefþjónustuna
		//		  og cache búið til eða sótt.
		protected List<Show> doInBackground(String... queries) {         
			TraktClient trackt = new TraktClient();	    	 
			List<Show> popularShows = (List<Show>) MainActivity.cache.get("popularShows");
	        
	        if(popularShows == null || popularShows.size() == 0) {
		    	Log.v("cache", "Popular shows not cached, retrieving new list");
		    	TraktClient trakt = new TraktClient();
		    	popularShows = trakt.popularShows();
		    	MainActivity.cache.put("popularShows", popularShows);
		    } else {
		    	Log.v("cahce", "Cached shows found");
		    	Log.v("cache", "Cache shows size: " + popularShows.size());
		    }
			return popularShows;
		}
		
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur verið stillt sem á að sýna á meðan notandi er að bíða
		protected void onPreExecute() {  
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(getResources().getString(R.string.list_process_title_popular));  
            progressDialog.setMessage(getResources().getString(R.string.list_process_msg_popular)); 
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
				button.setOnClickListener(new View.OnClickListener() {
					//Notkun: dbHelper.saveShow(show)
					//Eftirskilyrï¿½i: show hefur veriï¿½ bï¿½tt ï¿½ gagnasafn eÃ°a tekiÃ° Ãºr Ã¾vÃ­
					public void onClick(View view) {
		            	if(((Button)view).getText().toString() == getResources().getString(R.string.search_add)) {
		            		dbHelper.saveShow(show);
		            		((Button) view).setText(getResources().getString(R.string.take_off_list));
		            		showDialog(show);
		            	} else {
		            		dbHelper.deleteShow(show);
		            		((Button) view).setText(getResources().getString(R.string.search_add));
		            	}
		            }
		        });
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