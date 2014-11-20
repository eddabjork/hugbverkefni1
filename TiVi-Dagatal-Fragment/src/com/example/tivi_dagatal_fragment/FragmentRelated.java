/**
 * Nafn: 		Kristin Fjola Tomasdottir
 * Dagsetning: 	13. november 2014
 * Markmi�: 	FragmentRelated er fragment sem birtir lista
 * 				af thattum sem eru svipadir tilviksbreytunni show
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

	@Override
	//Eftir: birtir fragmenti� me� svipu�um ��ttar��um
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
	
	public void setShow(Show show){
		this.show = show;
	}	
	
	//Notkun: onAttach(activity)
	//Eftir:  b�i� er a� tengja gagnagrunninn vi� fragmenti�
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        dbHelper = new DbUtils(activity);
    }
		
	/**
     * Nafn: 		Krist�n Fj�la T�masd�ttir
     * Dagsetning: 	13.n�vember 2014
     * Markmi�: 	Framkv�mir �r��avinnu til a� birta svipa�a ��tti 
     * 				fr� vef�j�nustu � fragmenti me� loadi.
     */   
	private class RelatedShowsTask extends AsyncTask<Show, Integer, List<Show>> {
		//Notkun: doInBackground(queries)
		//Eftir:  B�i� er a� n� � lista af ��ttum sem eru svipa�ir fyrsta ��ttinum � shows
		protected List<Show> doInBackground(Show... shows) {         
			TraktClient client = new TraktClient();	    	 
			List<Show> relatedShows = client.relatedShows(shows[0]);
			return relatedShows;
		}
		
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur veri� stillt sem � a� s�na � me�an notandi er a� b��a
		protected void onPreExecute() {  
    		progressDialog = LayoutUtils.showProgressDialog(R.string.popular_process_title, 
    				R.string.popular_process_msg, getActivity());		
        }  
		
		//Notkun: onPostExecute(relatedShows)
		//Eftir:  B�i� er a� taka relatedShows listann og birta ��ttara�irnar � listanum
		protected void onPostExecute(List<Show> relatedShows) {
			LinearLayout listLayout = LayoutUtils.getRegListLayout(relatedShows, getActivity(), dbHelper);
			scrollView.addView(listLayout);
			progressDialog.dismiss();
		}
	}
}
