package com.example.tivi_dagatal_fragment;

import java.util.List;

import Clients.TraktClient;
import Data.DbUtils;
import Dtos.Episode;
import Dtos.Show;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_popular, container, false);
		flushCash();
		scrollView = new ScrollView(getActivity());
		setLayout();
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
	
	public void setLayout(){
		new PopularShowsTask().execute();
	}
	
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
		
		protected void onProgressUpdate(Integer... progress) {
			//setProgressPercent(progress[0]);
		}
		
		protected void onPostExecute(List<Show> searchShows) {
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