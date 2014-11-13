package com.example.tivi_dagatal_fragment;

import java.util.List;

import Clients.TraktClient;
import Data.DbUtils;
import Dtos.Show;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

public class FragmentRelated extends Fragment{
	
	private ScrollView scrollView;
	private ProgressDialog progressDialog;
	private DbUtils dbHelper;
	private Show show;

	@Override
	//Eftir: birtir fragmenti� me� svipu�um ��ttar��um
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_related, container, false);
		
		scrollView = new ScrollView(getActivity());

		new RelatedShowsTask().execute(show);
		rootView = scrollView;
		
        return rootView;
    }
	
	public void setShow(Show show){
		this.show = show;
	}	
	
	//Notkun: onAttach(activity)
	//Eftir:  b�i� er a� tengja gagnagrunninn vi� fragmenti�
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        dbHelper = new DbUtils(activity);
    }
		
	/**
     * Nafn: 		Krist�n Fj�la T�masd�ttir
     * Dagsetning: 	13.n�vember 2014
     * Markmi�: 	Framkv�mir �r��avinnu til a� birta svipa�a ��tti 
     * 				fr� vef�j�nustu � fragmenti me� loadi.
     */   
	private class RelatedShowsTask extends AsyncTask<Show, Integer, List<Show>> {
		//Notkun: doInBackground(queries)
		//Eftir:  B�i� er a� n� � lista af ��ttum sem eru svipa�ir fyrsta ��ttinum � shows
		protected List<Show> doInBackground(Show... shows) {         
			TraktClient client = new TraktClient();	    	 
			List<Show> relatedShows = client.relatedShows(shows[0]);
			return relatedShows;
		}
		
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur veri� stillt sem � a� s�na � me�an notandi er a� b��a
		protected void onPreExecute() {  
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(getResources().getString(R.string.process_title_popular));  
            progressDialog.setMessage(getResources().getString(R.string.process_msg_popular)); 
            progressDialog.setCancelable(false);  
            progressDialog.setIndeterminate(false);  
            progressDialog.show();  
        }  
		
		//Notkun: onPostExecute(relatedShows)
		//Eftir:  B�i� er a� taka relatedShows listann og birta ��ttara�irnar � listanum
		protected void onPostExecute(List<Show> relatedShows) {
			LinearLayout llv = new LinearLayout(getActivity());
			llv.setOrientation(LinearLayout.VERTICAL);
			
			for (final Show show : relatedShows){
				TextView title = new TextView(getActivity());
				title.setText(show.getTitle());
				title.setPadding(10,0,0,0);
				
				ImageButton addButton = getAddButton(show);	
				addButton.setPadding(10,10,10,10);
				ImageButton infoButton = getInfoButton(show);
				infoButton.setPadding(10,10,10,10);
				
				RelativeLayout episodeLayout = getEpisodeLayout(title, addButton, infoButton);
				
				llv.addView(episodeLayout);
				llv.addView(makeLine());
			}
			scrollView.addView(llv);
			
			progressDialog.dismiss();
		}
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
		DialogFragment newFragment = PopUpPutOnCal.newInstance(show);
	    newFragment.show(getFragmentManager(), "dialog");
	}
}
