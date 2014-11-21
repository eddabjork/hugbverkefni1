/**
 * Nafn: 		Kristin Fjola Tomasdottir
 * Dagsetning: 	13. november 2014
 * Markmid: 	FragmentRelated er fragment sem birtir lista
 * 				af thattum sem eru svipadir tilviksbreytunni show
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

public class FragmentRelated extends Fragment{
	
	private ScrollView scrollView;
	private ProgressDialog progressDialog;
	private DbUtils dbHelper;
	private Show show;
	private List<String> open = new ArrayList<String>();

	@Override
	//Eftir: birtir fragmentid med svipudum thattarodum
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_related, container, false);
		
		scrollView = new ScrollView(getActivity());
		if(VariousUtils.isConnectedToInternet(getActivity())){
			new RelatedShowsTask().execute(show);
		} else {
			LayoutUtils.showNotConnectedMsg(getActivity());
			LayoutUtils.showNoResult(scrollView, getActivity());
		}
		rootView = scrollView;
		
        return rootView;
    }
	
	//Notkun: setShow(show)
	//Eftir:  show hefur verid stilltur a tilviksbreyturna show
	public void setShow(Show show){
		this.show = show;
	}	
	
	//Notkun: onAttach(activity)
	//Eftir:  Buid er ad tengja gagnagrunninn vid fragmentid
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        dbHelper = new DbUtils(activity);
    }
		
	/**
     * Nafn: 		Kristin Fjola Tomasdottir
     * Dagsetning: 	13.november 2014
     * Markmid: 	Framkvaemir thradaavinnu til ad birta svipada thaetti 
     * 				fra vefthjonustu a fragmenti med loadi.
     */   
	private class RelatedShowsTask extends AsyncTask<Show, Integer, List<Show>> {
		//Notkun: doInBackground(queries)
		//Eftir:  Buid er ad na i lista af thattum sem eru svipadir fyrsta thaettinum i shows
		protected List<Show> doInBackground(Show... shows) {         
			TraktClient client = new TraktClient();	    	 
			List<Show> relatedShows = client.relatedShows(shows[0]);
			return relatedShows;
		}
		
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur verid stillt sem a ad syna a medan notandi er ad bida
		protected void onPreExecute() {  
    		progressDialog = LayoutUtils.showProgressDialog(R.string.popular_process_title, 
    				R.string.popular_process_msg, getActivity());		
        }  
		
		//Notkun: onPostExecute(relatedShows)
		//Eftir:  Buid er ad taka relatedShows listann og birta thattaradirnar i listanum
		protected void onPostExecute(List<Show> relatedShows) {
			LinearLayout listLayout = LayoutUtils.getRegListLayout(relatedShows, getActivity(), dbHelper, open);
			scrollView.addView(listLayout);
			progressDialog.dismiss();
		}
	}
}
