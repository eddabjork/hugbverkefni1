package com.example.tivi_dagatal_fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

public class FragmentInfo extends Fragment{
	@Override
	//Eftir: Birtir fragmentið sem sýnir upplýsingar um appið
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_info, container, false);

		/*scrollView = new ScrollView(getActivity());
		new PopularShowsTask().execute();
		rootView = scrollView;*/
		
        return rootView;
    }

}
