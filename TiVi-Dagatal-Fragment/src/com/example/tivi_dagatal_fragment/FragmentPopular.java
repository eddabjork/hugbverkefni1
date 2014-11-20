/**
 * Nafn: 		Steinunn Friï¿½geirsdï¿½ttir
 * Dagsetning: 	30. oktï¿½ber 2014
 * Markmiï¿½: 	FragmentPopular er fragment sem birtir lista
 * 				af visï¿½lum ï¿½ï¿½ttum
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
	//Eftir: Birtir fragmentiï¿½ sem sï¿½nir vinsï¿½la ï¿½ï¿½tti
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
	//Eftir: Bï¿½iï¿½ aï¿½ tengja gagnagrunn viï¿½ fragmentiï¿½
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        dbHelper = new DbUtils(activity);
    }

	/**
     * Nafn: 		Steinunn Friï¿½geirsdï¿½ttir
     * Dagsetning: 	30. oktï¿½ber 2014
     * Markmiï¿½: 	Framkvï¿½mir ï¿½rï¿½ï¿½avinnu til aï¿½ birta vinsï¿½la ï¿½ï¿½tti 
     * 				frï¿½ vefï¿½jï¿½nustu ï¿½ fragmenti meï¿½ loadi.
     * 				Clasinn geymir einnig cache fyrir ï¿½ï¿½ttina svo
     * 				ï¿½aï¿½ ï¿½urfi ekki aï¿½ sï¿½kja alla ï¿½ï¿½ttina oft.
     */   
	private class PopularShowsTask extends AsyncTask<String, Integer, List<Show>> {
		//Notkun: doInBackground(queries)
		//Eftir:  ï¿½ï¿½ï¿½avinnslu ï¿½ bakgrunni er lokiï¿½
		//        ï¿½ ï¿½rï¿½ï¿½avinnslu hï¿½r er kallaï¿½ ï¿½ vefï¿½jï¿½nustuna
		//		  og cache bï¿½iï¿½ til eï¿½a sï¿½tt.
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
		// Eftir:  progressDialog hefur veriï¿½ stillt sem ï¿½ aï¿½ sï¿½na ï¿½ meï¿½an notandi er aï¿½ bï¿½ï¿½a
		protected void onPreExecute() {  
			progressDialog = LayoutUtils.showProgressDialog(R.string.popular_process_title, 
    				R.string.popular_process_msg, getActivity());	
        }  
		
		//Notkun: onPostExecute(searchShows)
		//Eftir:  Bï¿½iï¿½ er aï¿½ taka serchShows listann og
		//        birta ï¿½ï¿½ ï¿½samt takka til ï¿½ess aï¿½ bï¿½ta viï¿½ 
		//        ï¿½ï¿½ttarï¿½ï¿½ ï¿½ dagatal. Listinn er svo birtur.
		protected void onPostExecute(List<Show> searchShows) {
			LinearLayout listLayout = LayoutUtils.getRegListLayout(searchShows, getActivity(), dbHelper, open);
			scrollView.addView(listLayout);
			progressDialog.dismiss();
		}
	}
}