/**
 * Nafn: 		Steinunn Fri�geirsd�ttir
 * Dagsetning: 	30. okt�ber 2014
 * Markmi�: 	FragmentEpisopde er fragment sem birtir uppl�singar
 * 				um einstaka ��tti.
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
	//Eftir: B�i� a� birta uppl�singar um einstaka ��tt
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_cal, container, false);
		scrollView = new ScrollView(getActivity());
		//setLayout();
		view = scrollView;
        return view;
	}
	
	public void setShow(Episode episode){
		this.episode = episode;
		Log.v("Var a� s�kja episode", episode.getTitle());
	}	
}
