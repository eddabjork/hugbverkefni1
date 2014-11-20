/**
 * Nafn: 		Edda Bj�rk Konr��sd�ttir og J�hanna Agnes Magn�sd�ttir
 * Dagsetning: 	9. oktÃƒÂ³ber 2014
 * MarkmiÃƒÂ°: 	Fragment sem sÃƒÂ½nir ÃƒÅ¾ÃƒÂ¦ttirnir-mÃƒÂ­nir lista sem inniheldur 
 * 				alla ÃƒÂ¾ÃƒÂ¡ ÃƒÂ¾ÃƒÂ¦tti sem notandi hefur sett ÃƒÂ­ tilsvarandi lista
 * 				(td. ÃƒÂ­ gegnum search)
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
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
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

public class FragmentList extends Fragment {
	private Integer id;
	private List<String> open = new ArrayList<String>();
	private MainScrollView mainScrollView;
	private LinearLayout mainLayout;
	private ProgressDialog progressDialog;
	private Fragment frag = new FragmentEpisode();
	private FragmentRelated fragmentRelated;
	private String noBannerUrl = "http://slurm.trakt.us/images/banner.jpg";
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
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		myActivity = activity;
	}
	
	/**
	 * Nafn: 		KristÃƒÆ’Ã‚Â­n FjÃƒÆ’Ã‚Â³la TÃƒÆ’Ã‚Â³masdÃƒÆ’Ã‚Â³ttir
	 * Dagsetning: 	23. oktÃƒÆ’Ã‚Â³ber 2014
	 * MarkmiÃƒÆ’Ã‚Â°: 	GetAllShowsTask framkvÃƒÆ’Ã‚Â¦mir ÃƒÆ’Ã‚Â¾rÃƒÆ’Ã‚Â¡ÃƒÆ’Ã‚Â°avinnu sem nÃƒÆ’Ã‚Â¦r ÃƒÆ’Ã‚Â­ alla ÃƒÆ’Ã‚Â¾ÃƒÆ’Ã‚Â¦tti frÃƒÆ’Ã‚Â¡ gagnagrunni
	 * 				sem ÃƒÆ’Ã‚Â¡ aÃƒÆ’Ã‚Â° birta ÃƒÆ’Ã‚Â­ 'MÃƒÆ’Ã‚Â­nir ÃƒÆ’Ã‚Â¾ÃƒÆ’Ã‚Â¦ttir' og birtir ÃƒÆ’Ã‚Â¾ÃƒÆ’Ã‚Â¡
	 */
	private class GetAllShowsTask extends AsyncTask<Void, Integer, List<Show>> {
		// Notkun: shows = doInBackground(voids)
		// Eftir:  shows er listi af ÃƒÆ’Ã‚Â¾ÃƒÆ’Ã‚Â¡ttum sem ÃƒÆ’Ã‚Â¡ aÃƒÆ’Ã‚Â° birta ÃƒÆ’Ã‚Â­ 'MÃƒÆ’Ã‚Â­nir ÃƒÆ’Ã‚Â¾ÃƒÆ’Ã‚Â¦ttir'
		protected List<Show> doInBackground(Void... voids) {
			DbUtils dbHelper = new DbUtils(getActivity());
			List<Show> showList = dbHelper.getAllShows();
			return showList;
		}
		
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur veriÃƒÆ’Ã‚Â° stillt sem ÃƒÆ’Ã‚Â¡ aÃƒÆ’Ã‚Â° sÃƒÆ’Ã‚Â½na ÃƒÆ’Ã‚Â¡ meÃƒÆ’Ã‚Â°an notandi er aÃƒÆ’Ã‚Â° bÃƒÆ’Ã‚Â­ÃƒÆ’Ã‚Â°a
		protected void onPreExecute() {  
    		progressDialog = LayoutUtils.showProgressDialog(R.string.list_process_title, 
    				R.string.list_process_msg, getActivity());	
        }  
		
		// Notkun: onPostExecute(shows)
		// Eftir:  shows hafa veriÃƒÆ’Ã‚Â° birtir ÃƒÆ’Ã‚Â¡ 'ÃƒÆ’Ã…Â¾ÃƒÆ’Ã‚Â¦ttirnir mÃƒÆ’Ã‚Â­nir'
		protected void onPostExecute(List<Show> showList) {
			progressDialog.dismiss();
			mainLayout = new LinearLayout(getActivity());
	    	mainLayout.setOrientation(LinearLayout.VERTICAL);
	    	
	    	mainLayout = LayoutUtils.getMyEpsListLayout(showList, getActivity(), new DbUtils(getActivity()));
	    	
	    	mainScrollView.addView(mainLayout);
		}
	}
	
	/**
     * Nafn: 	   Edda BjÃƒÂ¶rk KonrÃƒÂ¡ÃƒÂ°sdÃƒÂ³ttir
     * Dagsetning: 30. oktÃƒÂ³ber 2014
     * MarkmiÃƒÂ°:   NÃƒÂ¡ ÃƒÂ­ upplÃƒÂ½singar um ÃƒÂ¾ÃƒÂ¡ttarÃƒÂ¶ÃƒÂ° og sÃƒÂ½na ÃƒÅ¾ÃƒÂ¦ttirnir mÃƒÂ­nir lista
     * 			   meÃƒÂ° upplÃƒÂ½singum
     * */
	public class ShowInfoTask extends AsyncTask<Show, Integer, Show> {
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur verið stillt sem birtist á meðan notandi bíður
		protected void onPreExecute() {  
    		progressDialog = LayoutUtils.showProgressDialog(R.string.show_process_title, 
    				R.string.show_process_msg, FragmentList.myActivity);	
        }  
		
		//Notkun:		 show = doInBackground(shows)
		//EftirskilyrÃƒÂ°i: show er ÃƒÂ¾ÃƒÂ¡tturinn sem inniheldur upplÃƒÂ½singar
		//				 sem nÃƒÂ¡ÃƒÂ° er ÃƒÂ­ ÃƒÂºtfrÃƒÂ¡ shows
		protected Show doInBackground(Show... shows) {
			Show show = new Show();
			if(VariousUtils.isConnectedToInternet(FragmentList.myActivity)){
				TraktClient client = new TraktClient();
				if(!open.contains(""+shows[0].getInfoMain().getId())) show = client.getShowInfo(shows[0]);
			}
			show.setInfoLayout(shows[0].getInfoLayout());
			show.setInfoMain(shows[0].getInfoMain());
			show.setScrollView(shows[0].getScrollView());
			show.setInfoButton(shows[0].getInfoButton());
			return show;
			
		}
		
		//Notkun:		 onPostExecute(show)
		//EftirskilyrÃƒÂ°i: BÃƒÂºiÃƒÂ° er aÃƒÂ° sÃƒÂ¦kja upplÃƒÂ½singar um ÃƒÂ¾ÃƒÂ¡ttinn show
		//				 og sÃƒÂ½na ÃƒÂ­ ÃƒÅ¾ÃƒÂ¦ttirnir mÃƒÂ­nir lista.
		protected void onPostExecute(Show show) {
			fragmentRelated = new FragmentRelated();
			LayoutUtils.setUpInfoLayout(show, fragmentRelated, open, FragmentList.myActivity, id, noBannerUrl, true);
            progressDialog.dismiss();
		}
	}
	
	public class SeasonEpisodesTask extends AsyncTask<Map<Show, Season>, Integer, List<Episode>> {
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur verið stillt sem birtist á meðan notandi bíður
		protected void onPreExecute() {  
    		progressDialog = LayoutUtils.showProgressDialog(R.string.ep_process_title, 
    				R.string.ep_process_msg, FragmentList.myActivity);
        }  
		
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
     * Nafn: 	   Edda BjÃƒÆ’Ã‚Â¶rk KonrÃƒÆ’Ã‚Â¡ÃƒÆ’Ã‚Â°sdÃƒÆ’Ã‚Â³ttir
     * Dagsetning: 30. oktÃƒÆ’Ã‚Â³ber 2014
     * MarkmiÃƒÂ¯Ã‚Â¿Ã‚Â½:   Manual scroll view sem erfir frÃƒÆ’Ã‚Â¡ ScrollView svo hÃƒÆ’Ã‚Â¦gt sÃƒÆ’Ã‚Â©
     * 			  aÃƒÆ’Ã‚Â° virkja ÃƒÆ’Ã‚Â¾aÃƒÆ’Ã‚Â° og ,,slÃƒÆ’Ã‚Â¶kkva ÃƒÆ’Ã‚Â¡ ÃƒÆ’Ã‚Â¾vÃƒÆ’Ã‚Â­'' ÃƒÆ’Ã‚Â­ appinu
     * */
	private class MainScrollView extends ScrollView {
		private boolean scrollable = true;
		
		public MainScrollView(Context context) {
			super(context);
		}
		
		//Notkun: touch = scrollview.onTouchEvent(event)
		//EftirskilyrÃƒÆ’Ã‚Â°i:  touch er true ef scrollview er virkt og event
		//				  er ACTION_DOWN, false annars
		public boolean onTouchEvent(MotionEvent event) {
			switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(scrollable) return super.onTouchEvent(event);
				return scrollable;
			default:
				return super.onTouchEvent(event);
			}
		}
		
		// Notkun: 		 interupt = scrollview.onInterceptTouchEvent(event)
		//EftirskilyrÃƒÆ’Ã‚Â°i: interupt er false ef scrollview er ekki virkt, skilar
		//				 annars sama og samnefnt fall ÃƒÆ’Ã‚Â­ ScrollView
		public boolean onInterceptTouchEvent(MotionEvent event) {
			if(!scrollable) return false;
			else return super.onInterceptTouchEvent(event);
		}
	}
	
	//Notkun:		 id = getNextId()
	//EftirskilyrÃƒÆ’Ã‚Â°i: id er nÃƒÆ’Ã‚Â¦sta lausa auÃƒÆ’Ã‚Â°kenni
	private int getNextId() {
		id = (id == null) ? 0 : id+1;
		return id;
	}
}