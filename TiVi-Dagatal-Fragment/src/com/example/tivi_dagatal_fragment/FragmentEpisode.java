/**
 * Nafn: 		Steinunn Friðgeirsdóttir
 * Dagsetning: 	30. október 2014
 * Markmið: 	FragmentEpisopde er fragment sem birtir upplýsingar
 * 				um einstaka þætti.
 */
package com.example.tivi_dagatal_fragment;

import Dtos.Episode;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

public class FragmentEpisode extends Fragment{
	private ScrollView scrollView;
	public Episode episode;
	
	@Override
	//Eftir: Búið að birta upplýsingar um einstaka þátt
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_cal, container, false);
		scrollView = new ScrollView(getActivity());
		//setLayout();
		view = scrollView;
        return view;
	}
	
	public void setShow(Episode episode){
		this.episode = episode;
		Log.v("Var að sækja episode", episode.getTitle());
	}	
}
