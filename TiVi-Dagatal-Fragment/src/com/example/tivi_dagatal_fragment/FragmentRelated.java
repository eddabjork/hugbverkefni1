/**
 * Nafn: 		Kristin Fjola Tomasdottir
 * Dagsetning: 	13. november 2014
 * Markmið: 	FragmentRelated er fragment sem birtir lista
 * 				af thattum sem eru svipadir tilviksbreytunni show
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
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class FragmentRelated extends Fragment{
	
	private ScrollView scrollView;
	private ProgressDialog progressDialog;
	private DbUtils dbHelper;
	private Show show;

	@Override
	//Eftir: birtir fragmentiï¿½ meï¿½ svipuï¿½um ï¿½ï¿½ttarï¿½ï¿½um
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_related, container, false);
		
		scrollView = new ScrollView(getActivity());
		if(VariousUtils.isConnectedToInternet(getActivity())){
			new RelatedShowsTask().execute(show);
		} else {
			LayoutUtils.showNotConnectedMsg(getActivity());
			LayoutUtils.showNoResult(scrollView, getActivity());
		}
		rootView = scrollView;
		
        return rootView;
    }
	
	public void setShow(Show show){
		this.show = show;
	}	
	
	//Notkun: onAttach(activity)
	//Eftir:  bï¿½iï¿½ er aï¿½ tengja gagnagrunninn viï¿½ fragmentiï¿½
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        dbHelper = new DbUtils(activity);
    }
		
	/**
     * Nafn: 		Kristï¿½n Fjï¿½la Tï¿½masdï¿½ttir
     * Dagsetning: 	13.nï¿½vember 2014
     * Markmiï¿½: 	Framkvï¿½mir ï¿½rï¿½ï¿½avinnu til aï¿½ birta svipaï¿½a ï¿½ï¿½tti 
     * 				frï¿½ vefï¿½jï¿½nustu ï¿½ fragmenti meï¿½ loadi.
     */   
	private class RelatedShowsTask extends AsyncTask<Show, Integer, List<Show>> {
		//Notkun: doInBackground(queries)
		//Eftir:  Bï¿½iï¿½ er aï¿½ nï¿½ ï¿½ lista af ï¿½ï¿½ttum sem eru svipaï¿½ir fyrsta ï¿½ï¿½ttinum ï¿½ shows
		protected List<Show> doInBackground(Show... shows) {         
			TraktClient client = new TraktClient();	    	 
			List<Show> relatedShows = client.relatedShows(shows[0]);
			return relatedShows;
		}
		
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur veriï¿½ stillt sem ï¿½ aï¿½ sï¿½na ï¿½ meï¿½an notandi er aï¿½ bï¿½ï¿½a
		protected void onPreExecute() {  
    		progressDialog = LayoutUtils.showProgressDialog(R.string.popular_process_title, 
    				R.string.popular_process_msg, getActivity());		
        }  
		
		//Notkun: onPostExecute(relatedShows)
		//Eftir:  Bï¿½iï¿½ er aï¿½ taka relatedShows listann og birta ï¿½ï¿½ttaraï¿½irnar ï¿½ listanum
		protected void onPostExecute(List<Show> relatedShows) {
			LinearLayout llv = new LinearLayout(getActivity());
			llv.setOrientation(LinearLayout.VERTICAL);
			WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int width = size.x;
			int pd = (int) width/32;
			for (final Show show : relatedShows){
				TextView title = new TextView(getActivity());
				title.setText(show.getTitle());
				title.setPadding(pd,0,pd,0);
				
				ImageButton addButton = getAddButton(show);	
				addButton.setPadding(pd,pd,pd,pd);
				ImageButton infoButton = getInfoButton(show);
				infoButton.setPadding(pd,pd,pd,pd);
				
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
	// Eftir:  pop-up hefur veriï¿½ birt sem bï¿½ï¿½ur uppï¿½ aï¿½ vista show ï¿½ dagatali 
	void showDialog(Show show) {
		DialogFragment newFragment = PopUpPutOnCal.newInstance(show);
	    newFragment.show(getFragmentManager(), "dialog");
	}
}
