/**
 * Nafn: 		Steinunn Friðgeirsdóttir
 * Dagsetning: 	30. október 2014
 * Markmið: 	FragmentSearchResults er fragment sem birtir lista
 * 				af þáttum sem notandi leitar eftir.
 */
package com.example.tivi_dagatal_fragment;

import java.util.List;

import Clients.TraktClient;
import Data.DbUtils;
import Dtos.Show;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class FragmentSearchResults extends Fragment{
	private DbUtils dbHelper;
	private ScrollView scrollView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);
		
		scrollView = new ScrollView(getActivity());
		
		Bundle bundle = this.getArguments();
		char[] aWord = bundle.getCharArray("key");
		String word =  new String(aWord);
		Log.v("Strengurinn er", word);
		
		setLayout(word);
		
		rootView = scrollView;
		
        return rootView;
    }
	
	public void setLayout(String word){
		new SearchShowsTask().execute(word);
	}
	
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        dbHelper = new DbUtils(activity);
    }

	private class SearchShowsTask extends AsyncTask<String, Integer, List<Show>> {
		protected List<Show> doInBackground(String... queries) {         
			TraktClient search = new TraktClient();	    	 
			List<Show> searchShows = search.searchShow(queries[0]);  
			return searchShows;
		}
		
		protected void onProgressUpdate(Integer... progress) {
			//setProgressPercent(progress[0]);
		}
		
		protected void onPostExecute(List<Show> searchShows) {
			LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT,
			LayoutParams.MATCH_PARENT);
			LinearLayout llv = new LinearLayout(getActivity());
			llv.setOrientation(LinearLayout.VERTICAL);
			for (final Show show : searchShows){
				TextView textView = new TextView(getActivity());
				textView.setText(show.getTitle());
				Log.v("ï¿½ï¿½ttur heitir ", show.getTitle());
				textView.setLayoutParams(lparams);
				Button button = new Button(getActivity());
				button.setText(getResources().getString(R.string.search_add));
				button.setLayoutParams(lparams);
				button.setOnClickListener(new View.OnClickListener() {
					//Notkun: dbHelper.saveShow(show)
					//Eftirskilyrï¿½i: show hefur veriï¿½ bï¿½tt ï¿½ gagnasafn eÃ°a tekiÃ° Ãºr Ã¾vÃ­
					public void onClick(View view) {
		            	if(((Button)view).getText().toString() == getResources().getString(R.string.search_add)) {
		            		dbHelper.saveShow(show);
		            		((Button) view).setText(getResources().getString(R.string.take_off_list));
		            		showDialog(show);
		            	} else {
		            		dbHelper.deleteShow(show);
		            		((Button) view).setText(getResources().getString(R.string.search_add));
		            	}
		            }
		        });
				//Bï¿½ta titli og takka ï¿½ linearlayout
				llv.addView(textView);
				llv.addView(button);
			}
			//Bï¿½ta linearlayoutinu ï¿½ scrollview
			scrollView.addView(llv);
			//Birta nï¿½ja viewiï¿½
			//setContentView(sv);	
		}
	}
	
	// Notkun: showDialog(show)
	// Eftir:  pop-up hefur verið birt sem býður uppá að vista show á dagatali 
	void showDialog(Show show) {
	    DialogFragment newFragment = PutOnCalPopUp.newInstance(R.string.popup_put_cal, show);
	    newFragment.show(getFragmentManager(), "dialog");
	}
	
}

