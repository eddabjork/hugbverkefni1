/**
 * Nafn: 		Steinunn Fridgeirsdottir
 * Dagsetning: 	30. oktober 2014
 * Markmid: 	FragmentPopular er fragment sem birtir lista
 * 				af vinsaelum thattum
 */
package com.example.tivi_dagatal_fragment;

import java.util.ArrayList;
import java.util.List;

import Clients.TraktClient;
import Data.DbUtils;
import Dtos.Show;
import Utils.LayoutUtils;
import Utils.VariousUtils;
import android.app.Activity;
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

public class FragmentPopular extends Fragment {
	private DbUtils dbHelper;
	private ScrollView scrollView;
	private ProgressDialog progressDialog;
	private String cacheKey = "popularShows";
	private List<String> open = new ArrayList<String>();
	
	@Override
	//Eftir: Birtir fragmentid sem synir vinsaela thaetti
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_popular, container, false);
		VariousUtils.flushCacheAfter12Hours(cacheKey);
		scrollView = new ScrollView(getActivity());
		if(VariousUtils.isConnectedToInternet(getActivity())){
			new PopularShowsTask().execute();
		} else {
			LayoutUtils.showNotConnectedMsg(getActivity());
			LayoutUtils.showNoResult(scrollView, getActivity());
		}
		rootView = scrollView;
		
        return rootView;
    }
	
	//Notkun: onAttach(activity)
	//Eftir: Buid er ad tengja gagnagrunn vid fragmentid
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        dbHelper = new DbUtils(activity);
    }

	/**
     * Nafn: 		Steinunn Fridgeirsdottir
     * Dagsetning: 	30. oktober 2014
     * Markmid: 	Framkvaemir thradaavinnu til ad birta vinsaela thaetti 
     * 				fra veftjonustu a fragmenti med loadi.
     * 				Klasinn geymir einnig cache fyrir thaettina svo
     * 				thad thurfi ekki ad saekja alla thaettina oft.
     */   
	private class PopularShowsTask extends AsyncTask<String, Integer, List<Show>> {
		//Notkun: doInBackground(queries)
		//Eftir:  Thradavinnslu i bakgrunni er lokid,
		//        kallad er a veftjhonustuna og cache buid til eda sott.
		protected List<Show> doInBackground(String... queries) {      
			List<Show> popularShows = (List<Show>) MainActivity.getCache().get(cacheKey);
	        
	        if(popularShows == null || popularShows.size() == 0) {
		    	Log.v("cache", "Popular shows not cached, retrieving new list");
		    	TraktClient trakt = new TraktClient();
		    	popularShows = trakt.popularShows();
		    	MainActivity.getCache().put(cacheKey, popularShows);
		    } else {
		    	Log.v("cache", "Cached shows found, size: " + popularShows.size());
		    }
			return popularShows;
		}
		
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur verid stillt sem a ad syna a medan notandi er ad bida
		protected void onPreExecute() {  
			progressDialog = LayoutUtils.showProgressDialog(R.string.popular_process_title, 
    				R.string.popular_process_msg, getActivity());	
        }  
		
		//Notkun: onPostExecute(searchShows)
		//Eftir:  Buid er ad taka serchShows listann og
		//        birta thvi asamt takka til thess ad baeta vid 
		//        thattarod a dagatal. Listinn er svo birtur.
		protected void onPostExecute(List<Show> searchShows) {
			LinearLayout listLayout = LayoutUtils.getRegListLayout(searchShows, getActivity(), dbHelper, open);
			scrollView.addView(listLayout);
			progressDialog.dismiss();
		}
	}
}