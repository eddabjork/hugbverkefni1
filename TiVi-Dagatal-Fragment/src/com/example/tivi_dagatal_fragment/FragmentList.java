/**
 * Nafn: 		Edda Bjï¿½rk Konrï¿½ï¿½sdï¿½ttir og Jï¿½hanna Agnes Magnï¿½sdï¿½ttir
 * Dagsetning: 	9. oktÃƒÆ’Ã‚Â³ber 2014
 * MarkmiÃƒÆ’Ã‚Â°: 	Fragment sem sÃƒÆ’Ã‚Â½nir ÃƒÆ’Ã…Â¾ÃƒÆ’Ã‚Â¦ttirnir-mÃƒÆ’Ã‚Â­nir lista sem inniheldur 
 * 				alla ÃƒÆ’Ã‚Â¾ÃƒÆ’Ã‚Â¡ ÃƒÆ’Ã‚Â¾ÃƒÆ’Ã‚Â¦tti sem notandi hefur sett ÃƒÆ’Ã‚Â­ tilsvarandi lista
 * 				(td. ÃƒÆ’Ã‚Â­ gegnum search)
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
	 * Nafn: 		KristÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â­n FjÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â³la TÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â³masdÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â³ttir
	 * Dagsetning: 	23. oktÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â³ber 2014
	 * MarkmiÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â°: 	GetAllShowsTask framkvÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¦mir ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¾rÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â°avinnu sem nÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¦r ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â­ alla ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¦tti frÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¡ gagnagrunni
	 * 				sem ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¡ aÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â° birta ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â­ 'MÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â­nir ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¦ttir' og birtir ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¡
	 */
	private class GetAllShowsTask extends AsyncTask<Void, Integer, List<Show>> {
		// Notkun: shows = doInBackground(voids)
		// Eftir:  shows er listi af ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¡ttum sem ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¡ aÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â° birta ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â­ 'MÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â­nir ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¦ttir'
		protected List<Show> doInBackground(Void... voids) {
			DbUtils dbHelper = new DbUtils(getActivity());
			List<Show> showList = dbHelper.getAllShows();
			return showList;
		}
		
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur veriÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â° stillt sem ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¡ aÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â° sÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â½na ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¡ meÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â°an notandi er aÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â° bÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â­ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â°a
		protected void onPreExecute() {  
    		progressDialog = LayoutUtils.showProgressDialog(R.string.list_process_title, 
    				R.string.list_process_msg, getActivity());	
        }  
		
		// Notkun: onPostExecute(shows)
		// Eftir:  shows hafa veriÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â° birtir ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¡ 'ÃƒÆ’Ã†â€™Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¦ttirnir mÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â­nir'
		protected void onPostExecute(List<Show> showList) {
			progressDialog.dismiss();
			mainLayout = new LinearLayout(getActivity());
	    	mainLayout.setOrientation(LinearLayout.VERTICAL);
	    	
	    	mainLayout = LayoutUtils.getMyEpsListLayout(showList, getActivity(), new DbUtils(getActivity()), open);
	    	
	    	mainScrollView.addView(mainLayout);
		}
	}
	
	//TODO: vantar klasal�singu
	public class SeasonEpisodesTask extends AsyncTask<Map<Show, Season>, Integer, List<Episode>> {
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur veriÃ° stillt sem birtist Ã¡ meÃ°an notandi bÃ­Ã°ur
		protected void onPreExecute() {  
    		progressDialog = LayoutUtils.showProgressDialog(R.string.ep_process_title, 
    				R.string.ep_process_msg, FragmentList.myActivity);
        }  
		
		//Notkun: episodeList = doInBackground(map<show,season>)
		//Eftir: BÃºiÃ° er aÃ° sÃ¦kja Ã¾Ã¦tti og setja Ã­ listann episodeList
		//		 sem eru Ã­ Ã¾Ã¦ttinu show Ã­ serÃ­unni season
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
		//Eftir: BÃºiÃ° er aÃ° sÃ½na episodeList Ã­ viÃ°mÃ³tinu Ã¡ viÃ°eigandi
		//		 staÃ°.
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
     * Nafn: 	   Edda BjÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¶rk KonrÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â°sdÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â³ttir
     * Dagsetning: 30. oktÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â³ber 2014
     * MarkmiÃƒÆ’Ã‚Â¯Ãƒâ€šÃ‚Â¿Ãƒâ€šÃ‚Â½:   Manual scroll view sem erfir frÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¡ ScrollView svo hÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¦gt sÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â©
     * 			  aÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â° virkja ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¾aÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â° og ,,slÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¶kkva ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¡ ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¾vÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â­'' ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â­ appinu
     * */
	private class MainScrollView extends ScrollView {
		private boolean scrollable = true;
		
		//TODO: vantar l�singu
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
		//Eftir: interupt er false ef scrollview er ekki virkt, skilar
		//		 annars sama og samnefnt fall ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â­ ScrollView
		public boolean onInterceptTouchEvent(MotionEvent event) {
			if(!scrollable) return false;
			else return super.onInterceptTouchEvent(event);
		}
	}
}