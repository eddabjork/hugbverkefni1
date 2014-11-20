/**
 * Nafn: 		Kristin Fjola Tomasdottir
 * Dagsetning: 	13. november 2014
 * Markmiï¿½: 	FragmentRelated er fragment sem birtir lista
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
	//Eftir: birtir fragmentiï¿½ meï¿½ svipuï¿½um ï¿½ï¿½ttarï¿½ï¿½um
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
	
	//TODO: vantar lýsingu
	public void setShow(Show show){
		this.show = show;
	}	
	
	//Notkun: onAttach(activity)
	//Eftir:  bï¿½iï¿½ er aï¿½ tengja gagnagrunninn viï¿½ fragmentiï¿½
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        dbHelper = new DbUtils(activity);
    }
		
	/**
     * Nafn: 		Kristï¿½n Fjï¿½la Tï¿½masdï¿½ttir
     * Dagsetning: 	13.nï¿½vember 2014
     * Markmiï¿½: 	Framkvï¿½mir ï¿½rï¿½ï¿½avinnu til aï¿½ birta svipaï¿½a ï¿½ï¿½tti 
     * 				frï¿½ vefï¿½jï¿½nustu ï¿½ fragmenti meï¿½ loadi.
     */   
	private class RelatedShowsTask extends AsyncTask<Show, Integer, List<Show>> {
		//Notkun: doInBackground(queries)
		//Eftir:  Bï¿½iï¿½ er aï¿½ nï¿½ ï¿½ lista af ï¿½ï¿½ttum sem eru svipaï¿½ir fyrsta ï¿½ï¿½ttinum ï¿½ shows
		protected List<Show> doInBackground(Show... shows) {         
			TraktClient client = new TraktClient();	    	 
			List<Show> relatedShows = client.relatedShows(shows[0]);
			return relatedShows;
		}
		
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur veriï¿½ stillt sem ï¿½ aï¿½ sï¿½na ï¿½ meï¿½an notandi er aï¿½ bï¿½ï¿½a
		protected void onPreExecute() {  
    		progressDialog = LayoutUtils.showProgressDialog(R.string.popular_process_title, 
    				R.string.popular_process_msg, getActivity());		
        }  
		
		//Notkun: onPostExecute(relatedShows)
		//Eftir:  Bï¿½iï¿½ er aï¿½ taka relatedShows listann og birta ï¿½ï¿½ttaraï¿½irnar ï¿½ listanum
		protected void onPostExecute(List<Show> relatedShows) {
			LinearLayout listLayout = LayoutUtils.getRegListLayout(relatedShows, getActivity(), dbHelper, open);
			scrollView.addView(listLayout);
			progressDialog.dismiss();
		}
	}
}
