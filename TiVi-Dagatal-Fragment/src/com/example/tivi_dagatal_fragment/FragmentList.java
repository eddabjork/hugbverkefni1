/**
 * Nafn: 		Edda Bj�rk Konr��sd�ttir og J�hanna Agnes Magn�sd�ttir
 * Dagsetning: 	9. oktÃƒÂ³ber 2014
 * MarkmiÃƒÂ°: 	Fragment sem sÃƒÂ½nir ÃƒÅ¾ÃƒÂ¦ttirnir-mÃƒÂ­nir lista sem inniheldur 
 * 				alla ÃƒÂ¾ÃƒÂ¡ ÃƒÂ¾ÃƒÂ¦tti sem notandi hefur sett ÃƒÂ­ tilsvarandi lista
 * 				(td. ÃƒÂ­ gegnum search)
 */

package com.example.tivi_dagatal_fragment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Clients.IMDbClient;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
	    	
	    	for(Show show : showList){
	    		addShow(show);
	    	}
	    	
	    	mainScrollView.addView(mainLayout);
		}
	}
	
	public void addShow(final Show show) {
		int width = VariousUtils.getScreenWidth(getActivity());
		int pd = (int) width/32;
		
		RelativeLayout episodeLayout = new RelativeLayout(getActivity());
		
		TextView title = new TextView(getActivity());
		title.setText(show.getTitle());
		title.setPadding(pd,0,0,0);
		
		ImageButton calendarButton = getCalButton(show);
		calendarButton.setId(1);
		calendarButton.setPadding(pd,pd,pd,pd);
		
		ImageButton deleteButton = new ImageButton(getActivity());
		deleteButton.setId(R.id.deleteButton);
		deleteButton.setImageResource(R.drawable.delete);
		deleteButton.setBackgroundColor(Color.TRANSPARENT);
		deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	showDialog(show);
            }
        });
		deleteButton.setPadding(pd,pd,pd,pd);
		
		final ImageButton infoButton = new ImageButton(getActivity());
		infoButton.setId(R.id.infoButton);
		infoButton.setImageResource(R.drawable.down_arrow);
		infoButton.setBackgroundColor(Color.TRANSPARENT);
		infoButton.setPadding(pd,pd,pd,pd);
		
		final ScrollView scrollView = new ScrollView(getActivity());
		final LinearLayout infoLayout = new LinearLayout(getActivity());
		infoLayout.setOrientation(LinearLayout.VERTICAL);
		final LinearLayout infoMain = new LinearLayout(getActivity());
		infoMain.setOrientation(LinearLayout.VERTICAL);
		
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		infoLayout.setLayoutParams(layoutParams);
		infoLayout.setGravity(Gravity.CENTER);
		infoMain.setLayoutParams(layoutParams);
		infoMain.setVisibility(View.GONE);
		infoMain.setId(getNextId());
	
		View.OnClickListener infoButtonListener = new View.OnClickListener() {
			@Override 
			public void onClick(View view) {
				show.setInfoLayout(infoLayout);
				show.setInfoMain(infoMain);
				show.setScrollView(scrollView);
				show.setInfoButton(infoButton);
				new ShowInfoTask().execute(show);
			}
		};
		infoButton.setOnClickListener(infoButtonListener);
		
		RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams calParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams delParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams infoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		titleParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		titleParams.addRule(RelativeLayout.CENTER_VERTICAL);
		calParams.addRule(RelativeLayout.LEFT_OF, R.id.deleteButton);
		calParams.addRule(RelativeLayout.CENTER_VERTICAL);
		delParams.addRule(RelativeLayout.LEFT_OF, R.id.infoButton);
		delParams.addRule(RelativeLayout.CENTER_VERTICAL);
		infoParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		infoParams.addRule(RelativeLayout.CENTER_VERTICAL);
		
		episodeLayout.addView(title, titleParams);
		episodeLayout.addView(calendarButton, calParams);
		episodeLayout.addView(deleteButton, delParams);
		episodeLayout.addView(infoButton, infoParams);
		mainLayout.addView(episodeLayout);
		mainLayout.addView(infoMain);
		mainLayout.addView(makeLine());
	}
	
	//Notkun:		 calButton = getCalButton(show)
  	//EftirskilyrÃƒÆ’Ã‚Â°i: calButton er takki sem sÃƒÆ’Ã‚Â©r um ÃƒÆ’Ã‚Â° bÃƒÆ’Ã‚Â¦ta/taka ÃƒÆ’Ã‚Â¾ÃƒÆ’Ã‚Â¡ttinn show
	//				 af dagatali
	public ImageButton getCalButton(final Show show){
		final ImageButton calendarButton = new ImageButton(getActivity());
		DbUtils dbHelper = new DbUtils(getActivity());
		// 0 -> onCal=false; 1 -> onCal=true
		boolean onCal = dbHelper.isOnCal(show);
		if(onCal) {
			calendarButton.setImageResource(R.drawable.on_cal);
			calendarButton.setTag(1);
		}
		else {
			calendarButton.setImageResource(R.drawable.off_cal);
			calendarButton.setTag(0);
		}
		
		calendarButton.setPadding(3,6,3,6);
		calendarButton.setBackgroundColor(Color.TRANSPARENT);
		
		calendarButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent me) {
				if (me.getAction() == MotionEvent.ACTION_DOWN) {
					calendarButton.setColorFilter(Color.argb(150, 155, 155, 155));
					return true;
				} else if (me.getAction() == MotionEvent.ACTION_UP) {
					calendarButton.setColorFilter(Color.argb(0, 155, 155, 155));
					final int status =(Integer) view.getTag();
					if(status == 1) {
						removeFromCal(show);
						view.setTag(0);
						calendarButton.setImageResource(R.drawable.off_cal);
					}
					else {
						addToCal(show);
						view.setTag(1);
						calendarButton.setImageResource(R.drawable.on_cal);
					}
					return true;
				}
				return false;
			}
			
		});
		return calendarButton;
	}
	
	//Notkun:		 addToCal(show);
  	//EftirskilyrÃƒÆ’Ã‚Â°i: BÃƒÆ’Ã‚ÂºiÃƒÆ’Ã‚Â° er aÃƒÆ’Ã‚Â° uppfÃƒÆ’Ã‚Â¦ra gagnagrunn ÃƒÆ’Ã‚Â¾.a. gildiÃƒÆ’Ã‚Â° on_calendar=true fyrir show.
	public void addToCal(Show show){
		DbUtils dbHelper = new DbUtils(getActivity());
		dbHelper.putShowOnCal(show);
		VariousUtils.flushCache("calendarEpisodes");
	}
	//Notkun:		 removeFromCal(show);
  	//EftirskilyrÃƒÆ’Ã‚Â°i: BÃƒÆ’Ã‚ÂºiÃƒÆ’Ã‚Â° er aÃƒÆ’Ã‚Â° uppfÃƒÆ’Ã‚Â¦ra gagnagrunn ÃƒÆ’Ã‚Â¾.a. gildiÃƒÆ’Ã‚Â° on_calendar=false fyrir show.
	public void removeFromCal(Show show){
		DbUtils dbHelper = new DbUtils(getActivity());
		dbHelper.takeShowOffCal(show);
		VariousUtils.flushCache("calendarEpisodes");
	}
	
	//Notkun:		 removeFromMyEpisodes(show);
  	//EftirskilyrÃƒÆ’Ã‚Â°i: BÃƒÆ’Ã‚ÂºiÃƒÆ’Ã‚Â° er eyÃƒÆ’Ã‚Â°a ÃƒÆ’Ã‚Âºt show ÃƒÆ’Ã‚Âºr gagnagrunni.
	public void removeFromMyEpisodes(Show show){
		DbUtils dbHelper = new DbUtils(getActivity());
		dbHelper.deleteShow(show);
		VariousUtils.flushCache("calendarEpisodes");
		FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                       .replace(R.id.content_frame, new FragmentList())
                       .commit();
	}
	
	//Notkun:		 line = makeLine();
  	//EftirskilyrÃƒÆ’Ã‚Â°i: line er nÃƒÆ’Ã‚Âºna view hlutur sem er einfÃƒÆ’Ã‚Â¶ld, ÃƒÆ’Ã‚Â¾unn, grÃƒÆ’Ã‚Â¡ lÃƒÆ’Ã‚Â­na.
	public View makeLine(){
		 View v = new View(getActivity());
		 v.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 1, (float) 0.80));
		 v.setBackgroundColor(Color.rgb(203,203,203));
		 return v;
	}
	
	//Notkun:		 image = getImage(show);
  	//EftirskilyrÃƒÆ’Ã‚Â°i: image er poster mynd fyrir show og er ÃƒÆ’Ã‚Â­ rÃƒÆ’Ã‚Â©ttri stÃƒÆ’Ã‚Â¦rÃƒÆ’Ã‚Â°
	public ImageView getImage(Show show){
		ImageView image = new ImageView(getActivity());
		image.setImageResource(R.drawable.app_icon);
		return image;
	}
	
	/**
     * Nafn: KristÃƒÆ’Ã‚Â­n FjÃƒÆ’Ã‚Â³la TÃƒÆ’Ã‚Â³masdÃƒÆ’Ã‚Â³ttir
     * Dagsetning: 9. oktÃƒÆ’Ã‚Â³ber 2014
     * MarkmiÃƒÆ’Ã‚Â°: NÃƒÆ’Ã‚Â¦r ÃƒÆ’Ã‚Â­ myndir meÃƒÆ’Ã‚Â° samhliÃƒÆ’Ã‚Â°a ÃƒÆ’Ã‚Â¾rÃƒÆ’Ã‚Â¡ÃƒÆ’Ã‚Â°avinnslu
     * */
	public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;
		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}
		
		//Notkun:		 bm = doInBackground(urls);
	  	//EftirskilyrÃƒÆ’Ã‚Â°i: bm er myndin sem er sÃƒÆ’Ã‚Â³tt frÃƒÆ’Ã‚Â¡ urls.
		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}
		
		//Notkun:		 onPostExecute(result);
	  	//EftirskilyrÃƒÆ’Ã‚Â°i: bÃƒÆ’Ã‚ÂºiÃƒÆ’Ã‚Â° er aÃƒÆ’Ã‚Â° setja myndina result ÃƒÆ’Ã‚Â­ rÃƒÆ’Ã‚Â©tt ImageView.
		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
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
    				R.string.show_process_msg, getActivity());	
        }  
		
		//Notkun:		 show = doInBackground(shows)
		//EftirskilyrÃƒÂ°i: show er ÃƒÂ¾ÃƒÂ¡tturinn sem inniheldur upplÃƒÂ½singar
		//				 sem nÃƒÂ¡ÃƒÂ° er ÃƒÂ­ ÃƒÂºtfrÃƒÂ¡ shows
		protected Show doInBackground(Show... shows) {
			Show show = new Show();
			if(VariousUtils.isConnectedToInternet(getActivity())){
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
			LayoutUtils.setUpInfoLayout(show, fragmentRelated, open, getActivity(), id, noBannerUrl, true);
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
			int pd = (int) width/32;
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
	
	//Notkun:		 bm = fixBitmapSize(orgBm);
  	//EftirskilyrÃƒÆ’Ã‚Â°i: bm er nÃƒÆ’Ã‚Â¦stum sama bitmap og orgBm nema ÃƒÆ’Ã‚Â­ rÃƒÆ’Ã‚Â©ttri stÃƒÆ’Ã‚Â¦rÃƒÆ’Ã‚Â°
	public Bitmap fixBitmapSize(Bitmap originalBmp){
		int x = originalBmp.getWidth();
		int y = originalBmp.getHeight();
		int startX;
		int startY;
		Bitmap scaledBmp;
		double scale;
		
		int width = 100;
		int height = 100;
		
		if(x >= y){
			scale = y/height;
			scaledBmp = Bitmap.createScaledBitmap(originalBmp, (int)(x/scale), 100, false);
			x = scaledBmp.getWidth();
			y = scaledBmp.getHeight();
			startY = 0;
			startX = (x-y)/2;
		}
		else{
			scale = x/width;
			scaledBmp = Bitmap.createScaledBitmap(originalBmp, 100, (int)(y/scale), false);
			x = scaledBmp.getWidth();
			y = scaledBmp.getHeight();
			startX = 0;
			startY = (y-x)/2;
		}
		
		return Bitmap.createBitmap(scaledBmp, startX,startY,width,height);
	}	
	
	// Notkun: showDialog(show)
	// Eftir:  pop-up hefur verid birt sem spyr hvort notandi vilji eyda thaetti
	void showDialog(Show show) {
	    DialogFragment newFragment = PopUpDelete.newInstance(show);
	    newFragment.show(getFragmentManager(), "dialog");
	}
	/**
	 * Nafn: 		Krist�n Fj�la T�masd�ttir
	 * Dagsetning: 	11. n�vember 2014
	 * Markmi�: 	IMDbRatingTask framkv�mir �r��avinnu sem n�r � IMDb einkunn fyrir ��ttar��
	 */
	public class IMDbRatingTask extends AsyncTask<Map<Show, TextView>, Integer, Map<Show, TextView>> {
		// Notkun: map = doInBackground(maps)
		// Eftir:  map inniheldur ��tt me� einkunn og textasv��i sem � a� birta einkunn �
		protected Map<Show, TextView> doInBackground(Map<Show, TextView>... maps) {
			Map<Show, TextView> map = new HashMap<Show, TextView>();
			for(Show show : maps[0].keySet()){
				IMDbClient.getIMDbRating(show);
				map.put(show, maps[0].get(show));
			}
			return map;
		}
		
		// Notkun: onPostExecute(map)
		// Eftir:  einkunn � ��tti hefur veri� birt
		protected void onPostExecute(Map<Show, TextView> map) {
			for(Show show : map.keySet()){
				if(show.getImdbRating() != null) {
					map.get(show).setText(FragmentList.myActivity.getString(R.string.imdb_grade) + " " + show.getImdbRating());
				} else {
					map.get(show).setText(FragmentList.myActivity.getString(R.string.imdb_grade_not_found));
				}
			}
		}
	}
}