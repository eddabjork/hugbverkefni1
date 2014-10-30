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
					//Eftirskilyr�i: show hefur veri� b�tt � gagnasafn eða tekið úr því
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
				//B�ta titli og takka � linearlayout
				llv.addView(textView);
				llv.addView(button);
			}
			//B�ta linearlayoutinu � scrollview
			scrollView.addView(llv);
			//Birta n�ja viewi�
			//setContentView(sv);	
		}
	}
	
	void showDialog(Show show) {
	    DialogFragment newFragment = PutOnCalPopUp.newInstance(R.string.popup_put_cal, show);
	    newFragment.show(getFragmentManager(), "dialog");
	}
}