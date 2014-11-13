/**
 * Nafn: 		Steinunn Friðgeirsdóttir
 * Dagsetning: 	30. október 2014
 * Markmið: 	FragmentSearchResults er fragment sem birtir lista
 * 				af þáttum sem notandi leitar eftir.
 */
package com.example.tivi_dagatal_fragment;

import java.util.List;

import Clients.TraktClient;
import Data.DbUtils;
import Dtos.Show;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class FragmentSearchResults extends Fragment{
	private DbUtils dbHelper;
	private ScrollView scrollView;
	private ProgressDialog progressDialog;
	
	@Override
	//Eftir: birtir fragmentið með leitarniðurstöðunum
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);
		
		scrollView = new ScrollView(getActivity());
		
		Bundle bundle = this.getArguments();
		char[] aWord = bundle.getCharArray("key");
		String word =  new String(aWord);
		Log.v("Strengurinn er", word);

		new SearchShowsTask().execute(word);
		rootView = scrollView;
		
        return rootView;
    }
	
	//Notkun: onAttach(activity)
	//Eftir:  búið er að tengja gagnagrunninn við fragmentið
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        dbHelper = new DbUtils(activity);
    }

	/**
     * Nafn: 		Steinunn Friðgeirsdóttir
     * Dagsetning: 	30. október 2014
     * Markmið: 	Framkvæmir þráðavinnu til að birta leitarniðurstöður
     * 				frá vefþjónustu í fargmenti. 
     */   	
	private class SearchShowsTask extends AsyncTask<String, Integer, List<Show>> {
		//Notkun: 	doInBackground(queries)
		//Eftir:	Búið er að búa til vefþjónustu og sækja þá þætti sem 
		//			notandi leitaði eftir í bakgrunnsþærði og skila þeim.
		protected List<Show> doInBackground(String... queries) {         
			TraktClient search = new TraktClient();	    	 
			List<Show> searchShows = search.searchShow(queries[0]);  
			return searchShows;
		}
		
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
		
		//Eftir: Ekki í notkun	
		protected void onProgressUpdate(Integer... progress) {
			//setProgressPercent(progress[0]);
		}
		
		//Notkun:	onPostExecute(searchShows)
		//Eftir: 	Búið er að taka searchShows listann og birta hann.
		//          Í listanum eru líka takkar sem hægt er að ýta á og 
		// 	        þá bætist þáttur í gagnagrunn. 
		protected void onPostExecute(List<Show> searchShows) {
			LinearLayout llv = new LinearLayout(getActivity());
			llv.setOrientation(LinearLayout.VERTICAL);
			
			for (final Show show : searchShows){
				TextView title = new TextView(getActivity());
				title.setText(show.getTitle());
				Log.v("Thattur heitir ", show.getTitle());
				
				ImageButton addButton = getAddButton(show);				
				ImageButton infoButton = getInfoButton(show);
				
				RelativeLayout episodeLayout = getEpisodeLayout(title, addButton, infoButton);
				
				llv.addView(episodeLayout);
			}
			scrollView.addView(llv);
			progressDialog.dismiss();
		}
	}
	
	ImageButton getAddButton(final Show show){
		final ImageButton addButton = new ImageButton(getActivity());
		// 0 -> onList=false; 1 -> onList=true
		addButton.setTag(0);
		addButton.setImageResource(R.drawable.off_list);
		
		addButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent me) {
				if (me.getAction() == MotionEvent.ACTION_DOWN) {
					addButton.setColorFilter(Color.argb(150, 155, 155, 155));
					return true;
				} else if (me.getAction() == MotionEvent.ACTION_UP) {
					addButton.setColorFilter(Color.argb(0, 155, 155, 155));
					int status =(Integer) view.getTag();
	            	if(status == 0) {
	            		dbHelper.saveShow(show);
	            		view.setTag(1);
						addButton.setImageResource(R.drawable.on_list);
	            		showDialog(show);
	            	} else {
	            		dbHelper.deleteShow(show);
	            		view.setTag(0);
						addButton.setImageResource(R.drawable.off_list);
	            	}
					return true;
				}
				return false;
			}
			
		});
		addButton.setBackgroundColor(Color.TRANSPARENT);
		return addButton;
	}
	
	ImageButton getInfoButton(final Show show){
		ImageButton infoButton = new ImageButton(getActivity());
		infoButton.setId(1);
		infoButton.setImageResource(R.drawable.down_arrow);
		infoButton.setBackgroundColor(Color.TRANSPARENT);
		return infoButton;
	}
	
	RelativeLayout getEpisodeLayout(TextView title, ImageButton addButton, ImageButton infoButton){
		RelativeLayout episodeLayout = new RelativeLayout(getActivity());
		
		RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams addParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams infoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

		titleParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		titleParams.addRule(RelativeLayout.CENTER_VERTICAL);
		infoParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		infoParams.addRule(RelativeLayout.CENTER_VERTICAL);
		addParams.addRule(RelativeLayout.LEFT_OF, 1);
		addParams.addRule(RelativeLayout.CENTER_VERTICAL);
		
		episodeLayout.addView(title, titleParams);
		episodeLayout.addView(addButton, addParams);
		episodeLayout.addView(infoButton, infoParams);
		
		return episodeLayout;
	}
	
	// Notkun: showDialog(show)
	// Eftir:  pop-up hefur verið birt sem býður uppá að vista show á dagatali 
	void showDialog(Show show) {
	    DialogFragment newFragment = PopUpPutOnCal.newInstance( show);
	    newFragment.show(getFragmentManager(), "dialog");
	}
	
}

