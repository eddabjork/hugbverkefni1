package com.example.tivi_dagatal_fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class FragmentSearch extends Fragment {
	Fragment results;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search, container, false);
		results = new FragmentSearchResults();
        return rootView;
	}
	
	public void searchClick(View view){
		Fragment results = new FragmentSearchResults();
		
		EditText wordText = (EditText) getView().findViewById(R.id.leitarbox);
		String word = wordText.getText().toString();
		Log.v("Word er hér ", word);
		char[] aWord = word.toCharArray();
		String key = "key";
		Bundle bundle = new Bundle();
		bundle.putCharArray(key, aWord);
		results.setArguments(bundle);
		
		// Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                       .replace(R.id.content_frame, results)
                       .commit();
	}
	
}
