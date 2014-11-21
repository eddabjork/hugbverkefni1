/**
 * Nafn: 		Kristin Fjola Tomasdottir
 * Dagsetning: 	13. november 2014
 * Markmi�: 	FragmentInfo er fragment sem birtir upplysingar
 * 				og leidbeiningar um appid
 */
package com.example.tivi_dagatal_fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentInfo extends Fragment{
	@Override
	//Notkun: onCreateView(inflater, container, savedInstanceState)
	//Eftir:  Birtir fragmentid sem synir upplysingar um appid
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_info, container, false);
		
        return rootView;
    }

}
