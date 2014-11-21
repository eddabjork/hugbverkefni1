/**
 * Nafn: 		Steinunn Fridgeirsdottir
 * Dagsetning: 	30. oktober 2014
 * Markmid: 	FragmentSearchResults er fragment sem birtir lista
 * 				af thattum sem notandi leitar eftir.
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class FragmentSearchResults extends Fragment{
	private DbUtils dbHelper;
	private ScrollView scrollView;
	private ProgressDialog progressDialog;
	private List<String> open = new ArrayList<String>();
	
	@Override
	//Eftir: birtir fragmentid med leitarnidurstodunum
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);
		
		scrollView = new ScrollView(getActivity());
		
		Bundle bundle = this.getArguments();
		char[] aWord = bundle.getCharArray("search");
		String word =  new String(aWord);
		if(VariousUtils.isConnectedToInternet(getActivity())){
			new SearchShowsTask().execute(word);
		} else {
			LayoutUtils.showNotConnectedMsg(getActivity());
			LayoutUtils.showNoResult(scrollView, getActivity());
		}
		rootView = scrollView;
		
        return rootView;
    }
	
	//Notkun: onAttach(activity)
	//Eftir:  buid er ad tengja gagnagrunninn vid fragmentid
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        dbHelper = new DbUtils(activity);
    }

	/**
     * Nafn: 		Steinunn Fridgeirsdottir
     * Dagsetning: 	30. oktober 2014
     * Markmid: 	Framkvaemir thradavinnu til ad birta leitarnidurstodur
     * 				fra vefthjonustu i fragmenti. 
     */   	
	private class SearchShowsTask extends AsyncTask<String, Integer, List<Show>> {
		//Notkun: 	doInBackground(queries)
		//Eftir:	Buid er ad bua til vefthjonustu og saekja tha thaetti sem 
		//			notandi leitadi eftir i bakgrunnsthraedi og skila theim.
		protected List<Show> doInBackground(String... queries) {         
			TraktClient search = new TraktClient();	    	 
			List<Show> searchShows = search.searchShow(queries[0]);  
			return searchShows;
		}
		
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur verid stillt sem a ad syna a medan notandi er ad bida
		protected void onPreExecute() {  
    		progressDialog = LayoutUtils.showProgressDialog(R.string.list_process_title, 
    				R.string.search_process_msg, getActivity());
        } 
		
		//Notkun:	onPostExecute(searchShows)
		//Eftir: 	Buid er ad taka searchShows listann og birta hann.
		//          I listanum eru lika takkar sem haegt er ad yta a og 
		// 	        tha baetist thattur i gagnagrunn. 
		protected void onPostExecute(List<Show> searchShows) {
			LinearLayout listLayout = LayoutUtils.getRegListLayout(searchShows, getActivity(), dbHelper, open);
			scrollView.addView(listLayout);
			progressDialog.dismiss();
		}
	}
}

