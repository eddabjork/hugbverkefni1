package com.example.tivi_dagatal_fragment;

import java.util.List;

import Clients.TraktClient;
import Data.DbUtils;
import Dtos.Show;
import android.app.ActionBar.LayoutParams;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class FragmentSearchResults extends Fragment{
	DbUtils dbHelper = new DbUtils(this.getActivity());
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);
        testingSearch();
        return rootView;
    }

	public void testingSearch(){
		EditText wordText = (EditText) getView().findViewById(R.id.leitarbox);
		String word = wordText.toString();
		
		TraktClient search = new TraktClient();	    	 
		List<Show> searchShows = search.searchShow(word);
		
		LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
				ScrollView sv = new ScrollView(null);
				LinearLayout llv = new LinearLayout(null);
				llv.setOrientation(LinearLayout.VERTICAL);
				for (final Show show : searchShows){
					TextView textView = new TextView(null);
					textView.setText(show.getTitle());
					textView.setLayoutParams(lparams);
					Button button = new Button(null);
					button.setText(getResources().getString(R.string.search_add));
					button.setLayoutParams(lparams);
					button.setOnClickListener(new View.OnClickListener() {
						//Notkun: dbHelper.saveShow(show)
						//Eftirskilyr�i: show hefur veri� b�tt � gagnasafn eða tekið úr því
						public void onClick(View view) {
			            	if(((Button)view).getText().toString() == getResources().getString(R.string.search_add)) {
			            		dbHelper.saveShow(show);
			            		((Button) view).setText(getResources().getString(R.string.take_off_list));
			            	} else {
			            		dbHelper.deleteShow(show);
			            		((Button) view).setText(getResources().getString(R.string.search_add));
			            	}
			            }
			        });
					//B�ta titli og takka � linearlayout
					llv.addView(textView);
					llv.addView(button);
				}
				//B�ta linearlayoutinu � scrollview
				sv.addView(llv);
				//Birta n�ja viewi�
				//setContentView(sv);	
			}
}
	
	/*//Notkun: SearchStuff(view)
	//Eftirskilyr�i: B�i� er a� b�a til n�tt view sem inniheldur
	//				 leitarni�urst��ur fr� �v� or�i sem var � leitarboxi
	//				 �samt t�kkum til a� b�ta birtum ��ttar��um � lista.
	public View SearchStuff(View view){
		EditText wordText = (EditText) getView().findViewById(R.id.leitarbox);
		String word = wordText.getText().toString();		
		
		new SearchShowsTask().execute(word);
        
 		return view;
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
			ScrollView sv = new ScrollView(null);
			LinearLayout llv = new LinearLayout(null);
			llv.setOrientation(LinearLayout.VERTICAL);
			for (final Show show : searchShows){
				TextView textView = new TextView(null);
				textView.setText(show.getTitle());
				textView.setLayoutParams(lparams);
				Button button = new Button(null);
				button.setText(getResources().getString(R.string.search_add));
				button.setLayoutParams(lparams);
				button.setOnClickListener(new View.OnClickListener() {
					//Notkun: dbHelper.saveShow(show)
					//Eftirskilyr�i: show hefur veri� b�tt � gagnasafn eða tekið úr því
					public void onClick(View view) {
		            	if(((Button)view).getText().toString() == getResources().getString(R.string.search_add)) {
		            		dbHelper.saveShow(show);
		            		((Button) view).setText(getResources().getString(R.string.take_off_list));
		            	} else {
		            		dbHelper.deleteShow(show);
		            		((Button) view).setText(getResources().getString(R.string.search_add));
		            	}
		            }
		        });
				//B�ta titli og takka � linearlayout
				llv.addView(textView);
				llv.addView(button);
			}
			//B�ta linearlayoutinu � scrollview
			sv.addView(llv);
			//Birta n�ja viewi�
			//setContentView(sv);	
		}
	}*/

