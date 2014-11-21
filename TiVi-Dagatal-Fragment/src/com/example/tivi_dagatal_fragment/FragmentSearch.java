/**
 * Nafn: 		Steinunn Fridgeirsdottir
 * Dagsetning: 	30. oktober 2014
 * Markmid: 	FragmentSearch er fragment sem birtir leitarglugga
 * 				og takka til ad leita.
 * 
 */
package com.example.tivi_dagatal_fragment;

import Utils.VariousUtils;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;

public class FragmentSearch extends Fragment {
	Fragment results;
	
	@Override
	//Eftir: Birtir fragment med leitarglugga og takka
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        
        LinearLayout mainLayout = new LinearLayout(getActivity());
		mainLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		mainLayout.setOrientation(LinearLayout.HORIZONTAL);
		
        setLayout(mainLayout);
		view = mainLayout;
        return view;
	}
	
	//Notkun: setLayout();
	//Eftir: Buid er ad setja upp leitarutlitid
	public void setLayout(LinearLayout mainLayout){
		final EditText editText = new EditText(getActivity());
		TableLayout.LayoutParams params = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);
		params.setMargins(10,10,10,10);
		editText.setLayoutParams(params);
		editText.setId(R.id.leitarbox);
		editText.setOnKeyListener(new OnKeyListener() {
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
		          searchClick(v);
		          return true;
		        }
		        return false;
		    }
		});
		
		ImageButton button = new ImageButton(getActivity());
		button.setImageResource(R.drawable.search);
		button.setBackgroundColor(Color.TRANSPARENT);
		button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	searchClick(view);
            }
        });
		
		mainLayout.addView(editText);
		mainLayout.addView(button);
	}
	
	//Notkun: searchClick(view)
	//Eftir: Tekur thad sem er i leitarboxinu og leitar eftir thvi
	//       i gegnum vefthjonustu og skilar nidurstodum i nyju
	//       fragmenti sem er birt thegar nidurstodur eru komnar.
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
