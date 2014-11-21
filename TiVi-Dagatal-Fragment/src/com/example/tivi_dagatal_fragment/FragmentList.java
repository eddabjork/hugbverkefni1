/**
 * Nafn: 		Edda Bjork Konradsdottir og Johanna Agnes Magnusdottir
 * Dagsetning: 	9. oktober 2014
 * Markmid: 	Fragment sem synir thaettirnir minir lista sem inniheldur 
 * 				allar thaer thattaradir sem notandi hefur sett a tilsvarandi lista
 * 				(td. i gegnum search)
 */

package com.example.tivi_dagatal_fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Clients.TraktClient;
import Data.DbUtils;
import Dtos.Episode;
import Dtos.Season;
import Dtos.Show;
import Utils.LayoutUtils;
import Utils.VariousUtils;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class FragmentList extends Fragment {
	private Integer id;
	private List<String> open = new ArrayList<String>();
	private MainScrollView mainScrollView;
	private LinearLayout mainLayout;
	private ProgressDialog progressDialog;
	private Fragment frag = new FragmentEpisode();
	private FragmentRelated fragmentRelated;
	private static Activity myActivity;
	
	/** Sets the view */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cal, container, false);
        
		mainScrollView = new MainScrollView(getActivity());
		new GetAllShowsTask().execute();
		view = mainScrollView;
        return view;
	}
	
	/** Sets the appropriate activity when fragment attached*/
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		myActivity = activity;
	}
	
	/**
	 * Nafn: 		Kristin Fjola Tomasdottir
	 * Dagsetning: 	23. oktober 2014
	 * Markmid: 	GetAllShowsTask framkvaemir�thradavinnu sem naer i allar thattaradir fra gagnagrunni
	 * 				sem a ad birta i 'thaettirnir minir' og birtir a�skja
	 */
	private class GetAllShowsTask extends AsyncTask<Void, Integer, List<Show>> {
		// Notkun: shows = doInBackground(voids)
		// Eftir:  shows er listi af thattum sem a ad birta i 'Thaettirnir Minir'
		protected List<Show> doInBackground(Void... voids) {
			DbUtils dbHelper = new DbUtils(getActivity());
			List<Show> showList = dbHelper.getAllShows();
			return showList;
		}
		
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur verid stillt a thad sem a ad syna a medan notandi er ad bida
		protected void onPreExecute() {  
    		progressDialog = LayoutUtils.showProgressDialog(R.string.list_process_title, 
    				R.string.list_process_msg, getActivity());	
        }  
		
		// Notkun: onPostExecute(shows)
		// Eftir:  shows hafa verid birtir a�skjanum 'thaettirnir minir'
		protected void onPostExecute(List<Show> showList) {
			progressDialog.dismiss();
			mainLayout = new LinearLayout(getActivity());
	    	mainLayout.setOrientation(LinearLayout.VERTICAL);
	    	
	    	mainLayout = LayoutUtils.getMyEpsListLayout(showList, getActivity(), new DbUtils(getActivity()), open);
	    	
	    	mainScrollView.addView(mainLayout);
		}
	}
	
	//Nafn: Edda Bjork Konradsdottir
	//Dagsetning: 13. november 2014
	//Markmid: Na i thaetti fyrir seriu og syna a videigandi stad i upplysingum
	//		   um thattarod
	public class SeasonEpisodesTask extends AsyncTask<Map<Show, Season>, Integer, List<Episode>> {
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur verid stillt sem birtist a medan notandi bidur
		protected void onPreExecute() {  
    		progressDialog = LayoutUtils.showProgressDialog(R.string.ep_process_title, 
    				R.string.ep_process_msg, FragmentList.myActivity);
        }  
		
		//Notkun: episodeList = doInBackground(map<show,season>)
		//Eftir: Buid er ad saekja thaetti og setja i listann episodeList
		//		 sem eru i thaettinu show i seriunni season
		protected List<Episode> doInBackground(Map<Show, Season>... map) {
			TraktClient client = new TraktClient();
			Season season = new Season();
			ImageButton infoButton = new ImageButton(FragmentList.myActivity);
			LinearLayout episodes = new LinearLayout(FragmentList.myActivity);
			for(Show key : map[0].keySet()) {
				episodes = map[0].get(key).getEpisodesView();
				infoButton = key.getInfoButton();
				if(!open.contains(""+episodes.getId())) season = client.getEpisodesForSeasonForShowInfo(key, map[0].get(key));
			}
			List<Episode> episodeList = new ArrayList<Episode>();
			if(open.contains(""+episodes.getId())) {
				episodeList.add(new Episode());
			} else {
				episodeList = season.getEpisodes();
			}
			episodeList.get(0).setEpisodesView(episodes);
			episodeList.get(0).setInfoButton(infoButton);
			return episodeList;
		}
		
		//Notkun: onPostExecute(episodeList)
		//Eftir: Buid er ad syna episodeList i vidmotinu a videigandi
		//		 stad.
		protected void onPostExecute(List<Episode> episodeList) {
			int width = VariousUtils.getScreenWidth(FragmentList.myActivity);
			int pd = (int) width/6;
			LinearLayout episodes = episodeList.get(0).getEpisodesView();
			ImageButton infoButton = episodeList.get(0).getInfoButton();
			
			if(!open.contains(""+episodes.getId())) {
				episodes.removeAllViews();
				for(final Episode episode : episodeList) {
					TextView textView = new TextView(FragmentList.myActivity);
				    textView.setText(episode.getNumber()+". "+episode.getTitle());
				    textView.setPadding(pd,0,0,0);
				    textView.setGravity(Gravity.LEFT);
				    textView.setTextSize(15);
				    textView.setOnClickListener(new View.OnClickListener() {
						public void onClick(View view) {
							((FragmentEpisode) frag).setEpisode(episode);
							FragmentManager fragmentManager = FragmentList.myActivity.getFragmentManager();
							VariousUtils.addFragmentToStack(fragmentManager, frag);
							FragmentList.myActivity.getActionBar().setTitle(episode.getShowTitle());
						}
					});
					episodes.addView(textView);
				}
			}
			Animator.setHeightForWrapContent(FragmentList.myActivity, episodes);
			Animator animation = null;
            if(open.contains(""+episodes.getId())) {
                animation = new Animator(episodes, 500, 1);
                open.remove(""+episodes.getId());
                infoButton.setImageResource(R.drawable.down_arrow);
            } else {
                animation = new Animator(episodes, 500, 0);
                open.add(""+episodes.getId());
                infoButton.setImageResource(R.drawable.up_arrow);
            }
            progressDialog.dismiss();
            episodes.startAnimation(animation);
		}
	}
	
	/**
     * Nafn: 	   Edda Bjork Konradsdottir
     * Dagsetning: 30. oktober 2014
     * Markmid:   Manual scroll view sem erfir fra ScrollView svo haegt se
     * 			  ad virkja thad og slokkva a thvi i appinu
     * */
	private class MainScrollView extends ScrollView {
		private boolean scrollable = true;
		
		//Notkun: mainScrollView = new MainScrollView(context)
		//Eftirskilyrdi: mainScrollView er nytt MainScrollView (erfir fra
		//				 ScrollView)
		public MainScrollView(Context context) {
			super(context);
		}
		
		//Notkun: touch = scrollview.onTouchEvent(event)
		//Eftir:  touch er true ef scrollview er virkt og event
		//		  er ACTION_DOWN, false annars
		public boolean onTouchEvent(MotionEvent event) {
			switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(scrollable) return super.onTouchEvent(event);
				return scrollable;
			default:
				return super.onTouchEvent(event);
			}
		}
		
		// Notkun: interupt = scrollview.onInterceptTouchEvent(event)
		// Eftir: interupt er false ef scrollview er ekki virkt, skilar
		//		 annars sama og samnefnt fall i ScrollView
		public boolean onInterceptTouchEvent(MotionEvent event) {
			if(!scrollable) return false;
			else return super.onInterceptTouchEvent(event);
		}
	}
}