/**
 * Nafn: 		Steinunn Fri�geirsd�ttir
 * Dagsetning: 	13. n�vember 2014
 * Markmi�: 	FragmentStart er fragment sem er startskj�rinn
 */
package com.example.tivi_dagatal_fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentStart extends Fragment{
	@Override
	//Eftir: Birtir Velkomin fragmenti� sem birtist � byrjun appsins
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_start, container, false);
        return rootView;
    }
}