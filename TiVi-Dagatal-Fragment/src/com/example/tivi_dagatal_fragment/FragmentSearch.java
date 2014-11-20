/**
 * Nafn: 		Steinunn Fri�geirsd�ttir
 * Dagsetning: 	30. okt�ber 2014
 * Markmi�: 	FragmentSearch er fragment sem birtir leitarglugga
 * 				og takka til a� leita.
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
	//Eftir: Birtir fragment me� leitarglugga og takka
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
	//Eftir: B�i� er a� setja upp leitar�tliti�
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
	//Eftir: Tekur �a� sem er � leitarboxinu og leitar eftir �v�
	//       � gegnum vef�j�nustu og skilar ni�urst��um � n�ju
	//       fragmenti sem er birt �egar ni�urst��ur eru komnar.
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
