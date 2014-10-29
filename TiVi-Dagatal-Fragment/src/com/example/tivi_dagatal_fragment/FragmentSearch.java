package com.example.tivi_dagatal_fragment;

import java.util.List;

import Clients.TraktClient;
import Data.DbUtils;
import Dtos.Show;
import android.app.ActionBar.LayoutParams;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class FragmentSearch extends Fragment {
	DbUtils dbHelper = new DbUtils(this.getActivity());
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        
		//rootView = SearchStuff(rootView);

		setEnterKeyListener();
        return rootView;
		//return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	// veit ekki hvernig þetta er í síma, en mjög hægt í emulator :(
		public void setEnterKeyListener(){
			EditText editText = (EditText) getView().findViewById(R.id.leitarbox);
			/*editText.setOnKeyListener(new OnKeyListener()
			{
			    public boolean onKey(View v, int keyCode, KeyEvent event)
			    {
			        if (event.getAction() == KeyEvent.ACTION_DOWN)
			        {
			            switch (keyCode)
			            {
			                case KeyEvent.KEYCODE_DPAD_CENTER:
			                case KeyEvent.KEYCODE_ENTER:
			                	//SearchStuff(v);
			                    return true;
			                default:
			                    break;
			            }
			        }
			        return false;
			    }
			});*/
		}
		
		//Notkun: SearchStuff(view)
		//Eftirskilyrï¿½i: Bï¿½iï¿½ er aï¿½ bï¿½a til nï¿½tt view sem inniheldur
		//				 leitarniï¿½urstï¿½ï¿½ur frï¿½ ï¿½vï¿½ orï¿½i sem var ï¿½ leitarboxi
		//				 ï¿½samt tï¿½kkum til aï¿½ bï¿½ta birtum ï¿½ï¿½ttarï¿½ï¿½um ï¿½ lista.
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
					textView.setTextSize(30);
					textView.setLayoutParams(lparams);
					Button button = new Button(null);
					button.setText(getResources().getString(R.string.search_add));
					button.setLayoutParams(lparams);
					button.setOnClickListener(new View.OnClickListener() {
						//Notkun: dbHelper.saveShow(show)
						//Eftirskilyrï¿½i: show hefur veriï¿½ bï¿½tt ï¿½ gagnasafn eÃ°a tekiÃ° Ãºr Ã¾vÃ­
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
					//Bï¿½ta titli og takka ï¿½ linearlayout
					llv.addView(textView);
					llv.addView(button);
				}
				//Bï¿½ta linearlayoutinu ï¿½ scrollview
				sv.addView(llv);
				//Birta nï¿½ja viewiï¿½
				//setContentView(sv);	
			}
		}
}
