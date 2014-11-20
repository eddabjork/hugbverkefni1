/**
 * Nafn: 		Steinunn Fri�geirsd�ttir
 * Dagsetning: 	30. okt�ber 2014
 * Markmi�: 	FragmentPopular er fragment sem birtir lista
 * 				af vis�lum ��ttum
 */
package com.example.tivi_dagatal_fragment;

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
	
	@Override
	//Eftir: Birtir fragmenti� sem s�nir vins�la ��tti
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
	//Eftir: B�i� a� tengja gagnagrunn vi� fragmenti�
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        dbHelper = new DbUtils(activity);
    }

	/**
     * Nafn: 		Steinunn Fri�geirsd�ttir
     * Dagsetning: 	30. okt�ber 2014
     * Markmi�: 	Framkv�mir �r��avinnu til a� birta vins�la ��tti 
     * 				fr� vef�j�nustu � fragmenti me� loadi.
     * 				Clasinn geymir einnig cache fyrir ��ttina svo
     * 				�a� �urfi ekki a� s�kja alla ��ttina oft.
     */   
	private class PopularShowsTask extends AsyncTask<String, Integer, List<Show>> {
		//Notkun: doInBackground(queries)
		//Eftir:  ���avinnslu � bakgrunni er loki�
		//        � �r��avinnslu h�r er kalla� � vef�j�nustuna
		//		  og cache b�i� til e�a s�tt.
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
		// Eftir:  progressDialog hefur veri� stillt sem � a� s�na � me�an notandi er a� b��a
		protected void onPreExecute() {  
			progressDialog = LayoutUtils.showProgressDialog(R.string.popular_process_title, 
    				R.string.popular_process_msg, getActivity());	
        }  
		
		//Notkun: onPostExecute(searchShows)
		//Eftir:  B�i� er a� taka serchShows listann og
		//        birta �� �samt takka til �ess a� b�ta vi� 
		//        ��ttar�� � dagatal. Listinn er svo birtur.
		protected void onPostExecute(List<Show> searchShows) {
			LinearLayout listLayout = LayoutUtils.getRegListLayout(searchShows, getActivity(), dbHelper);
			scrollView.addView(listLayout);
			progressDialog.dismiss();
		}
	}
}