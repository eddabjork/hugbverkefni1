package com.example.tivi_dagatal_fragment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Clients.TraktClient;
import Data.DbUtils;
import Dtos.Season;
import Dtos.Show;
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
import android.view.ViewGroup;
import android.widget.Button;
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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cal, container, false);
		
		/******TEST ï¿½ï¿½TTIR
		DbUtils dbHelper = new DbUtils(getActivity());
		Show show1 = new Show();
        show1.setTitle("Flash");
        show1.setDataTitle("the-flash-2014");
        show1.setPoster("kallaposter1");
        dbHelper.saveShow(show1);
        //
		Show show2 = new Show();
        show2.setTitle("Big Bang Theory");
        show2.setDataTitle("big-bang-theory");
        show2.setPoster("kallaposter2");
        dbHelper.saveShow(show2);
        //
        Show show3 = new Show();
        show3.setTitle("Arrow");
        show3.setDataTitle("arrow");
        show3.setPoster("kallaposter3");
        dbHelper.saveShow(show3);
        END TEST****/
        
        
		mainScrollView = new MainScrollView(getActivity());
		setLayout();
		view = mainScrollView;
        return view;
	}
	
	public void setLayout(){
		new GetAllShowsTask().execute();
	}
	
	/**
	 * Nafn: 		Kristín Fjóla Tómasdóttir
	 * Dagsetning: 	23. október 2014
	 * Markmið: 	GetAllShowsTask framkvæmir þráðavinnu sem nær í alla þætti frá gagnagrunni
	 * 				sem á að birta í 'Mínir þættir' og birtir þá
	 */
	private class GetAllShowsTask extends AsyncTask<Void, Integer, List<Show>> {
		// Notkun: shows = doInBackground(voids)
		// Eftir:  shows er listi af þáttum sem á að birta í 'Mínir þættir'
		protected List<Show> doInBackground(Void... voids) {
			DbUtils dbHelper = new DbUtils(getActivity());
			List<Show> showList = dbHelper.getAllShows();
			return showList;
		}
		
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur verið stillt sem á að sýna á meðan notandi er að bíða
		protected void onPreExecute() {  
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Ná í þætti..");  
            progressDialog.setMessage("Það er verið að ná í þættina þína.. chill out");  
            progressDialog.setCancelable(false);  
            progressDialog.setIndeterminate(false);  
            progressDialog.show();  
        }  
		
		// Notkun: onPostExecute(shows)
		// Eftir:  shows hafa verið birtir á 'Þættirnir mínir'
		protected void onPostExecute(List<Show> showList) {
			progressDialog.dismiss();
			mainLayout = new LinearLayout(getActivity());
	    	mainLayout.setOrientation(LinearLayout.VERTICAL);
	    	
	    	for(Show show : showList){
	    		new ShowInfoTask().execute(show);
	    	}
	    	
	    	mainScrollView.addView(mainLayout);
		}
	}
	
	public Button getCalButton(final Show show){
		final Button calendarButton = new Button(getActivity());
		DbUtils dbHelper = new DbUtils(getActivity());
		// 0 -> onCal=false; 1 -> onCal=true
		boolean onCal = dbHelper.isOnCal(show);
		if(onCal) {
			calendarButton.setText(getResources().getString(R.string.btn_rem_cal));
			calendarButton.setTag(1);
		}
		else {
			calendarButton.setText(getResources().getString(R.string.btn_put_cal));
			calendarButton.setTag(0);
		}
		calendarButton.setTextSize(10);
		calendarButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
				final int status =(Integer) view.getTag();
				if(status == 1) {
					remFromCal(show);
					view.setTag(0);
					calendarButton.setText(getResources().getString(R.string.btn_put_cal));
				}
				else {
					addToCal(show);
					view.setTag(1);
					calendarButton.setText(getResources().getString(R.string.btn_rem_cal));	
				}
            }
        });
		return calendarButton;
	}
	
	public void addToCal(Show show){
		DbUtils dbHelper = new DbUtils(getActivity());
		dbHelper.putShowOnCal(show);
		MainActivity.cache.remove("calendarEpisodes");
		Log.v("cache", "Calendar episodes removed from cache");
	}
	
	public void remFromCal(Show show){
		DbUtils dbHelper = new DbUtils(getActivity());
		dbHelper.takeShowOffCal(show);
		MainActivity.cache.remove("calendarEpisodes");
		Log.v("cache", "Calendar episodes removed from cache");
	}
	
	public void removeFromMyEpisodes(Show show){
		DbUtils dbHelper = new DbUtils(getActivity());
		dbHelper.deleteShow(show);
		FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                       .replace(R.id.content_frame, new FragmentList())
                       .commit();
	}
	
	public View makeLine(){
		 View v = new View(getActivity());
		 v.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 1, (float) 0.80));
		 v.setBackgroundColor(Color.rgb(203,203,203));
		 return v;
	}
	
	public ImageView getImage(Show show){
		ImageView image = new ImageView(getActivity());
		image.setImageResource(R.drawable.temp_icon);
		//String imgUrl = show.getPoster();
		//new DownloadImageTask(image).execute(imgUrl);
		//image.buildDrawingCache();
		return image;
	}
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;
		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}
		
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
			return fixBitmapSize(mIcon11);
		}
		
		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}
	
	private class ShowInfoTask extends AsyncTask<Show, Integer, Show> {
		protected Show doInBackground(Show... shows) {
			TraktClient client = new TraktClient();
			Show show = client.getShowInfo(shows[0]);
			return show;
		}
		
		protected void onProgressUpdate(Integer... progress) {
			//setProgressPercent(progress[0]);
		}
		/**************ÞARF AÐ SPLITTA ÞESSU NIÐUR**************************/
		protected void onPostExecute(Show show) {
			RelativeLayout episodeLayout = new RelativeLayout(getActivity());
			
			TextView title = new TextView(getActivity());
			title.setText(show.getTitle());
			title.setPadding(10,0,0,0);
			
			final Show _show = show;
			
			Button calendarButton = getCalButton(show);
			calendarButton.setId(1);
			calendarButton.setBackgroundResource(R.drawable.pretty_button);
			calendarButton.setPadding(5,0,5,0);
			
			Button deleteButton = new Button(getActivity());
			deleteButton.setId(2);
			deleteButton.setText(getResources().getString(R.string.btn_delete));
			deleteButton.setTextSize(10);
			deleteButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View view) {
	            	removeFromMyEpisodes(_show);
	            }
	        });
			deleteButton.setBackgroundResource(R.drawable.pretty_button);
			deleteButton.setPadding(5,0,5,0);
			
			ImageButton infoButton = new ImageButton(getActivity());
			infoButton.setId(3);
			infoButton.setImageResource(R.drawable.down_arrow);
			infoButton.setBackgroundColor(Color.TRANSPARENT);
			
			final ScrollView scrollView = new ScrollView(getActivity());
			LinearLayout infoLayout = new LinearLayout(getActivity());
			infoLayout.setOrientation(LinearLayout.VERTICAL);
			final LinearLayout infoMain = new LinearLayout(getActivity());
			infoMain.setOrientation(LinearLayout.VERTICAL);
			
			List<Season> seasons = show.getSeasons();
			Collections.reverse(seasons);
			for(Season season : seasons) {
				Log.v("Ã¾Ã¡ttarÃ¶Ã°",""+season.getSeasonNumber());
				TextView seasonbutton = new TextView(getActivity());
				seasonbutton.setText("SerÃ­a " + season.getSeasonNumber());
				seasonbutton.setGravity(Gravity.CENTER);
				seasonbutton.setTextSize(20);
				infoLayout.addView(seasonbutton);
				final TextView episodes = new TextView(getActivity());
				episodes.setVisibility(View.GONE);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				episodes.setLayoutParams(layoutParams);
				episodes.setGravity(Gravity.CENTER);
				episodes.setId(getNextId());
				episodes.setText("hér koma þættir");
				infoLayout.addView(episodes);
				Animator.setHeightForWrapContent(getActivity(), episodes);
				seasonbutton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						mainScrollView.setScrollingEnabled(false);
						Animator animation = null;
		                if(open.contains(""+episodes.getId())) {
		                    animation = new Animator(episodes, 500, 1);
		                    open.remove(""+episodes.getId());
		                } else {
		                    animation = new Animator(episodes, 500, 0);
		                    open.add(""+episodes.getId());
		                }
		                episodes.startAnimation(animation);
					}
				});
			}
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			infoLayout.setLayoutParams(layoutParams);
			infoLayout.setGravity(Gravity.CENTER);
			scrollView.addView(infoLayout);
			infoMain.setLayoutParams(layoutParams);
			infoMain.setVisibility(View.GONE);
			infoMain.setId(getNextId());
			infoMain.addView(scrollView);
			Animator.setHeightForWrapContent(getActivity(), infoMain);
			infoButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					mainScrollView.setScrollingEnabled(true);
					Animator animation = null;
	                if(open.contains(""+infoMain.getId())) {
	                    animation = new Animator(infoMain, 500, 1);
	                    open.remove(""+infoMain.getId());
	                } else {
	                    animation = new Animator(infoMain, 500, 0);
	                    open.add(""+infoMain.getId());
	                }
	                infoMain.startAnimation(animation);
				}
			});
			
			RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			RelativeLayout.LayoutParams calParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			//RelativeLayout.LayoutParams delParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			RelativeLayout.LayoutParams delParams = new RelativeLayout.LayoutParams(35, RelativeLayout.LayoutParams.WRAP_CONTENT);
			RelativeLayout.LayoutParams infoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			
			titleParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			titleParams.addRule(RelativeLayout.CENTER_VERTICAL);
			calParams.addRule(RelativeLayout.LEFT_OF, 2);
			calParams.addRule(RelativeLayout.CENTER_VERTICAL);
			delParams.addRule(RelativeLayout.LEFT_OF, 3);
			delParams.addRule(RelativeLayout.CENTER_VERTICAL);
			delParams.setMargins(5,5,0,0);
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
	}
	
	private class MainScrollView extends ScrollView {
		private boolean scrollable = true;
		
		public MainScrollView(Context context) {
			super(context);
		}
		
		public void setScrollingEnabled(boolean enabled) {
			scrollable = enabled;
		}
		
		public boolean isScrollable() {
			return scrollable;
		}
		
		public boolean onTouchEvent(MotionEvent event) {
			switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(scrollable) return super.onTouchEvent(event);
				return scrollable;
			default:
				return super.onTouchEvent(event);
			}
		}
		
		public boolean onInterceptTouchEvent(MotionEvent event) {
			if(!scrollable) return false;
			else return super.onInterceptTouchEvent(event);
		}
	}
	
	private int getNextId() {
		id = (id == null) ? 0 : id+1;
		return id;
	}
	
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
}