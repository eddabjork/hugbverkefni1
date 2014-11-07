/**
 * Nafn: 		Steinunn Fri�geirsd�ttir
 * Dagsetning: 	30. okt�ber 2014
 * Markmi�: 	FragmentSearchResults er fragment sem birtir lista
 * 				af ��ttum sem notandi leitar eftir.
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
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class FragmentSearchResults extends Fragment{
	private DbUtils dbHelper;
	private ScrollView scrollView;
	
	@Override
	//Eftir: birtir fragmenti� me� leitarni�urst��unum
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);
		
		scrollView = new ScrollView(getActivity());
		
		Bundle bundle = this.getArguments();
		char[] aWord = bundle.getCharArray("key");
		String word =  new String(aWord);
		Log.v("Strengurinn er", word);

		new SearchShowsTask().execute(word);
		rootView = scrollView;
		
        return rootView;
    }
	
	//Notkun: onAttach(activity)
	//Eftir:  b�i� er a� tengja gagnagrunninn vi� fragmenti�
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        dbHelper = new DbUtils(activity);
    }

	/**
     * Nafn: 		Steinunn Fri�geirsd�ttir
     * Dagsetning: 	30. okt�ber 2014
     * Markmi�: 	Framkv�mir �r��avinnu til a� birta leitarni�urst��ur
     * 				fr� vef�j�nustu � fargmenti. 
     */   	
	private class SearchShowsTask extends AsyncTask<String, Integer, List<Show>> {
		//Notkun: 	doInBackground(queries)
		//Eftir:	B�i� er a� b�a til vef�j�nustu og s�kja �� ��tti sem 
		//			notandi leita�i eftir � bakgrunns��r�i og skila �eim.
		protected List<Show> doInBackground(String... queries) {         
			TraktClient search = new TraktClient();	    	 
			List<Show> searchShows = search.searchShow(queries[0]);  
			return searchShows;
		}
		
		//Eftir: Ekki � notkun	
		protected void onProgressUpdate(Integer... progress) {
			//setProgressPercent(progress[0]);
		}
		
		//Notkun:	onPostExecute(searchShows)
		//Eftir: 	B�i� er a� taka searchShows listann og birta hann.
		//          � listanum eru l�ka takkar sem h�gt er a� �ta � og 
		// 	        �� b�tist ��ttur � gagnagrunn. 
		protected void onPostExecute(List<Show> searchShows) {
			LinearLayout llv = new LinearLayout(getActivity());
			llv.setOrientation(LinearLayout.VERTICAL);
			
			for (final Show show : searchShows){
				RelativeLayout episodeLayout = new RelativeLayout(getActivity());
				
				TextView title = new TextView(getActivity());
				title.setText(show.getTitle());
				title.setId(1);
				Log.v("Thattur heitir ", show.getTitle());
				
				Button addButton = new Button(getActivity());
				addButton.setId(2);
				addButton.setText(getResources().getString(R.string.search_add));
				addButton.setOnClickListener(new View.OnClickListener() {
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
				
				ImageButton infoButton = new ImageButton(getActivity());
				infoButton.setId(3);
				infoButton.setImageResource(R.drawable.down_arrow);
				infoButton.setBackgroundColor(Color.TRANSPARENT);
				
				//setja layout params � alla
				RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams addParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams infoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

				titleParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				titleParams.addRule(RelativeLayout.CENTER_VERTICAL);
				infoParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				infoParams.addRule(RelativeLayout.CENTER_VERTICAL);
				addParams.addRule(RelativeLayout.LEFT_OF, 3);
				addParams.addRule(RelativeLayout.CENTER_VERTICAL);
				
				episodeLayout.addView(title, titleParams);
				episodeLayout.addView(addButton, addParams);
				episodeLayout.addView(infoButton, infoParams);
				llv.addView(episodeLayout);
			}
			//B�ta linearlayoutinu � scrollview
			scrollView.addView(llv);
			//Birta n�ja viewi�
			//setContentView(sv);	
		}
	}
	
	// Notkun: showDialog(show)
	// Eftir:  pop-up hefur veri� birt sem b��ur upp� a� vista show � dagatali 
	void showDialog(Show show) {
	    DialogFragment newFragment = PutOnCalPopUp.newInstance(R.string.popup_put_cal, show);
	    newFragment.show(getFragmentManager(), "dialog");
	}
	
}

