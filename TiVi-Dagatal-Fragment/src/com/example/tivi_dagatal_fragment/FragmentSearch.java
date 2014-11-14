/**
 * Nafn: 		Steinunn Friðgeirsdóttir
 * Dagsetning: 	30. október 2014
 * Markmið: 	FragmentSearch er fragment sem birtir leitarglugga
 * 				og takka til að leita.
 * 
 */
package com.example.tivi_dagatal_fragment;

import Utils.VariousUtils;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class FragmentSearch extends Fragment {
	Fragment results;
	
	@Override
	//Eftir: Birtir fragment með leitarglugga og takka
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search, container, false);
		results = new FragmentSearchResults();
        return rootView;
	}
	
	//TODO: Setja inn onBackPressed stuff??
	
	//Notkun: searchClick(view)
	//Eftir: Tekur það sem er í leitarboxinu og leitar eftir því
	//       í gegnum vefþjónustu og skilar niðurstöðum í nýju
	//       fragmenti sem er birt þegar niðurstöður eru komnar.
	public void searchClick(View view){
		Fragment results = new FragmentSearchResults();
		
		EditText wordText = (EditText) getView().findViewById(R.id.leitarbox);
		String word = wordText.getText().toString();
		char[] aWord = word.toCharArray();
		String key = "search";
		Bundle bundle = new Bundle();
		bundle.putCharArray(key, aWord);
		results.setArguments(bundle);
		
		// hide keyboard
		InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(wordText.getWindowToken(), 0);
		
		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getFragmentManager();
		VariousUtils.addFragmentToStack(fragmentManager, results);
	}
	
}
