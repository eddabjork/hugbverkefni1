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
import Utils.LayoutUtils;
import Utils.VariousUtils;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

public class FragmentSearchResults extends Fragment{
	private DbUtils dbHelper;
	private ScrollView scrollView;
	private ProgressDialog progressDialog;
	
	@Override
	//Eftir: birtir fragmenti� me� leitarni�urst��unum
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);
		
		scrollView = new ScrollView(getActivity());
		
		Bundle bundle = this.getArguments();
		char[] aWord = bundle.getCharArray("search");
		String word =  new String(aWord);
		Log.v("Strengurinn er", word);
		if(VariousUtils.isConnectedToInternet(getActivity())){
			new SearchShowsTask().execute(word);
		} else {
			LayoutUtils.showNotConnectedMsg(getActivity());
			LayoutUtils.showNoResult(scrollView, getActivity());
		}
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
		
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur veri� stillt sem � a� s�na � me�an notandi er a� b��a
		protected void onPreExecute() {  
            progressDialog = new ProgressDialog(getActivity(), R.style.ProgressDialog);
            progressDialog.setTitle(getResources().getString(R.string.list_process_title));  
            progressDialog.setMessage(getResources().getString(R.string.search_process_msg)); 
            progressDialog.setCancelable(false);  
            progressDialog.setIndeterminate(false);  
            progressDialog.show();  
            // change color of divider
            int dividerId = progressDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
    		View divider = progressDialog.findViewById(dividerId);
    		divider.setBackgroundColor(getResources().getColor(R.color.app_red));
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
			WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int width = size.x;
			int pd = (int) width/32;
			
			LinearLayout llv = new LinearLayout(getActivity());
			llv.setOrientation(LinearLayout.VERTICAL);
			if(searchShows.isEmpty()){
				LayoutUtils.showNoResult(llv, getActivity(), true);
			}
			else{
				for (final Show show : searchShows){
					TextView title = new TextView(getActivity());
					title.setText(show.getTitle());
					title.setPadding(pd,0,0,0);
					Log.v("Thattur heitir ", show.getTitle());
					
					ImageButton addButton = getAddButton(show);
					addButton.setPadding(pd,pd,pd,pd);
					ImageButton infoButton = getInfoButton(show);
					infoButton.setPadding(pd,pd,pd,pd);
					
					RelativeLayout episodeLayout = getEpisodeLayout(title, addButton, infoButton);
					
					llv.addView(episodeLayout);
					llv.addView(makeLine());
				}
			}
			scrollView.addView(llv);
			progressDialog.dismiss();
		}
	
	
	ImageButton getAddButton(final Show show){
		final ImageButton addButton = new ImageButton(getActivity());
		// 0 -> onList=false; 1 -> onList=true
		addButton.setTag(0);
		addButton.setImageResource(R.drawable.off_list);
		
		addButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent me) {
				if (me.getAction() == MotionEvent.ACTION_DOWN) {
					addButton.setColorFilter(Color.argb(150, 155, 155, 155));
					return true;
				} else if (me.getAction() == MotionEvent.ACTION_UP) {
					addButton.setColorFilter(Color.argb(0, 155, 155, 155));
					int status =(Integer) view.getTag();
	            	if(status == 0) {
	            		dbHelper.saveShow(show);
	            		view.setTag(1);
						addButton.setImageResource(R.drawable.on_list);
	            		showDialog(show);
	            	} else {
	            		dbHelper.deleteShow(show);
	            		view.setTag(0);
						addButton.setImageResource(R.drawable.off_list);
	            	}
					return true;
				}
				return false;
			}
			
		});
		addButton.setBackgroundColor(Color.TRANSPARENT);
		return addButton;
	}
	
	ImageButton getInfoButton(final Show show){
		ImageButton infoButton = new ImageButton(getActivity());
		infoButton.setId(1);
		infoButton.setImageResource(R.drawable.down_arrow);
		infoButton.setBackgroundColor(Color.TRANSPARENT);
		return infoButton;
	}
	
	RelativeLayout getEpisodeLayout(TextView title, ImageButton addButton, ImageButton infoButton){
		RelativeLayout episodeLayout = new RelativeLayout(getActivity());
		
		RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams addParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams infoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

		titleParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		titleParams.addRule(RelativeLayout.CENTER_VERTICAL);
		infoParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		infoParams.addRule(RelativeLayout.CENTER_VERTICAL);
		addParams.addRule(RelativeLayout.LEFT_OF, 1);
		addParams.addRule(RelativeLayout.CENTER_VERTICAL);
		
		episodeLayout.addView(title, titleParams);
		episodeLayout.addView(addButton, addParams);
		episodeLayout.addView(infoButton, infoParams);
		
		return episodeLayout;
	}
	
	public View makeLine(){
		 View v = new View(getActivity());
		 v.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 1, (float) 0.80));
		 v.setBackgroundColor(Color.rgb(203,203,203));
		 return v;
	}
	
	// Notkun: showDialog(show)
	// Eftir:  pop-up hefur veri� birt sem b��ur upp� a� vista show � dagatali 
	void showDialog(Show show) {
	    DialogFragment newFragment = PopUpPutOnCal.newInstance( show);
	    newFragment.show(getFragmentManager(), "dialog");
	}
	
	}
}

