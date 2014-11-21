/**
 * Nafn: 		Steinunn Fridgeirsdottir
 * Dagsetning: 	13. november 2014
 * Markmid: 	FragmentStart er fragment sem er startskjarinn
 */
package com.example.tivi_dagatal_fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentStart extends Fragment{
	@Override
	//Eftir: Birtir Velkomin fragmentid sem birtist i byrjun appsins
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_start, container, false);
        return rootView;
    }
}
