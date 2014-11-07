/**
 * Nafn: 		Edda Björk Konráðsdóttir og Jóhanna Agnes Magnúsdóttir
 * Dagsetning: 	9. október 2014
 * Markmið: 	Fragment sem sýnir Þættirnir-mínir lista sem inniheldur 
 * 				alla þá þætti sem notandi hefur sett í tilsvarandi lista
 * 				(td. í gegnum search)
 */

package com.example.tivi_dagatal_fragment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import Clients.TraktClient;
import Data.DbUtils;
import Dtos.Episode;
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
	
	/** Sets the view */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cal, container, false);
        
		mainScrollView = new MainScrollView(getActivity());
		new GetAllShowsTask().execute();
		view = mainScrollView;
        return view;
	}
	
	/**
	 * Nafn: 		KristÃ­n FjÃ³la TÃ³masdÃ³ttir
	 * Dagsetning: 	23. oktÃ³ber 2014
	 * MarkmiÃ°: 	GetAllShowsTask framkvÃ¦mir Ã¾rÃ¡Ã°avinnu sem nÃ¦r Ã­ alla Ã¾Ã¦tti frÃ¡ gagnagrunni
	 * 				sem Ã¡ aÃ° birta Ã­ 'MÃ­nir Ã¾Ã¦ttir' og birtir Ã¾Ã¡
	 */
	private class GetAllShowsTask extends AsyncTask<Void, Integer, List<Show>> {
		// Notkun: shows = doInBackground(voids)
		// Eftir:  shows er listi af Ã¾Ã¡ttum sem Ã¡ aÃ° birta Ã­ 'MÃ­nir Ã¾Ã¦ttir'
		protected List<Show> doInBackground(Void... voids) {
			DbUtils dbHelper = new DbUtils(getActivity());
			List<Show> showList = dbHelper.getAllShows();
			return showList;
		}
		
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur veriÃ° stillt sem Ã¡ aÃ° sÃ½na Ã¡ meÃ°an notandi er aÃ° bÃ­Ã°a
		protected void onPreExecute() {  
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(getResources().getString(R.string.list_process_title));  
            progressDialog.setMessage(getResources().getString(R.string.list_process_msg));  
            progressDialog.setCancelable(false);  
            progressDialog.setIndeterminate(false);  
            progressDialog.show();  
        }  
		
		// Notkun: onPostExecute(shows)
		// Eftir:  shows hafa veriÃ° birtir Ã¡ 'ÃžÃ¦ttirnir mÃ­nir'
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
	
	//Notkun:		 calButton = getCalButton(show)
  	//EftirskilyrÃ°i: calButton er takki sem sÃ©r um Ã° bÃ¦ta/taka Ã¾Ã¡ttinn show
	//				 af dagatali
	public ImageButton getCalButton(final Show show){
		final ImageButton calendarButton = new ImageButton(getActivity());
		DbUtils dbHelper = new DbUtils(getActivity());
		// 0 -> onCal=false; 1 -> onCal=true
		boolean onCal = dbHelper.isOnCal(show);
		if(onCal) {
			calendarButton.setImageResource(R.drawable.rem_cal_btn);
			calendarButton.setTag(1);
		}
		else {
			calendarButton.setImageResource(R.drawable.put_cal_btn);
			calendarButton.setTag(0);
		}
		calendarButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
				final int status =(Integer) view.getTag();
				if(status == 1) {
					removeFromCal(show);
					view.setTag(0);
					calendarButton.setImageResource(R.drawable.put_cal_btn);
				}
				else {
					addToCal(show);
					view.setTag(1);
					calendarButton.setImageResource(R.drawable.rem_cal_btn);
				}
            }
        });
		calendarButton.setPadding(3,6,3,6);
		calendarButton.setBackgroundColor(Color.TRANSPARENT);
		return calendarButton;
	}
	
	//Notkun:		 addToCal(show);
  	//EftirskilyrÃ°i: BÃºiÃ° er aÃ° uppfÃ¦ra gagnagrunn Ã¾.a. gildiÃ° on_calendar=true fyrir show.
	public void addToCal(Show show){
		DbUtils dbHelper = new DbUtils(getActivity());
		dbHelper.putShowOnCal(show);
		MainActivity.cache.remove("calendarEpisodes");
		Log.v("cache", "Calendar episodes removed from cache");
	}
	//Notkun:		 removeFromCal(show);
  	//EftirskilyrÃ°i: BÃºiÃ° er aÃ° uppfÃ¦ra gagnagrunn Ã¾.a. gildiÃ° on_calendar=false fyrir show.
	public void removeFromCal(Show show){
		DbUtils dbHelper = new DbUtils(getActivity());
		dbHelper.takeShowOffCal(show);
		MainActivity.cache.remove("calendarEpisodes");
		Log.v("cache", "Calendar episodes removed from cache");
	}
	
	//Notkun:		 removeFromMyEpisodes(show);
  	//EftirskilyrÃ°i: BÃºiÃ° er eyÃ°a Ãºt show Ãºr gagnagrunni.
	public void removeFromMyEpisodes(Show show){
		DbUtils dbHelper = new DbUtils(getActivity());
		dbHelper.deleteShow(show);
		MainActivity.cache.remove("calendarEpisodes");
		Log.v("cache", "Calendar episodes removed from cache");
		FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                       .replace(R.id.content_frame, new FragmentList())
                       .commit();
	}
	
	//Notkun:		 line = makeLine();
  	//EftirskilyrÃ°i: line er nÃºna view hlutur sem er einfÃ¶ld, Ã¾unn, grÃ¡ lÃ­na.
	public View makeLine(){
		 View v = new View(getActivity());
		 v.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 1, (float) 0.80));
		 v.setBackgroundColor(Color.rgb(203,203,203));
		 return v;
	}
	
	//Notkun:		 image = getImage(show);
  	//EftirskilyrÃ°i: image er poster mynd fyrir show og er Ã­ rÃ©ttri stÃ¦rÃ°
	public ImageView getImage(Show show){
		ImageView image = new ImageView(getActivity());
		image.setImageResource(R.drawable.temp_icon);
		//String imgUrl = show.getPoster();
		//new DownloadImageTask(image).execute(imgUrl);
		//image.buildDrawingCache();
		return image;
	}
	
	/**
     * Nafn: KristÃ­n FjÃ³la TÃ³masdÃ³ttir
     * Dagsetning: 9. oktÃ³ber 2014
     * MarkmiÃ°: NÃ¦r Ã­ myndir meÃ° samhliÃ°a Ã¾rÃ¡Ã°avinnslu
     * */
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;
		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}
		
		//Notkun:		 bm = doInBackground(urls);
	  	//EftirskilyrÃ°i: bm er myndin sem er sÃ³tt frÃ¡ urls.
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
		
		//Notkun:		 onPostExecute(result);
	  	//EftirskilyrÃ°i: bÃºiÃ° er aÃ° setja myndina result Ã­ rÃ©tt ImageView.
		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}
	
	/**
     * Nafn: 	   Edda BjÃ¶rk KonrÃ¡Ã°sdÃ³ttir
     * Dagsetning: 30. oktÃ³ber 2014
     * MarkmiÃ°:   NÃ¡ Ã­ upplÃ½singar um Ã¾Ã¡ttarÃ¶Ã° og sÃ½na ÃžÃ¦ttirnir mÃ­nir lista
     * 			   meÃ° upplÃ½singum
     * */
	private class ShowInfoTask extends AsyncTask<Show, Integer, Show> {
		//Notkun:		 show = doInBackground(shows)
		//EftirskilyrÃ°i: show er Ã¾Ã¡tturinn sem inniheldur upplÃ½singar
		//				 sem nÃ¡Ã° er Ã­ ÃºtfrÃ¡ shows
		protected Show doInBackground(Show... shows) {
			TraktClient client = new TraktClient();
			Show show = client.getShowInfo(shows[0]);
			return show;
		}
		
		protected void onProgressUpdate(Integer... progress) {
			//setProgressPercent(progress[0]);
		}
		/**************ÃžARF AÃ� SPLITTA ÃžESSU NIÃ�UR**************************/
		//Notkun:		 onPostExecute(show)
		//EftirskilyrÃ°i: BÃºiÃ° er aÃ° sÃ¦kja upplÃ½singar um Ã¾Ã¡ttinn show
		//				 og sÃ½na Ã­ ÃžÃ¦ttirnir mÃ­nir lista. BÃºiÃ° er aÃ° setja
		//				 upp ÃºtlitiÃ° fyrir ÃžÃ¦ttirnir mÃ­nir lista
		protected void onPostExecute(final Show show) {
			RelativeLayout episodeLayout = new RelativeLayout(getActivity());
			
			TextView title = new TextView(getActivity());
			title.setText(show.getTitle());
			title.setPadding(10,0,0,0);
			
			final Show _show = show;
			
			ImageButton calendarButton = getCalButton(show);
			calendarButton.setId(1);
			
			ImageButton deleteButton = new ImageButton(getActivity());
			deleteButton.setId(2);
			deleteButton.setImageResource(R.drawable.del_btn);
			deleteButton.setPadding(3,6,3,6);
			deleteButton.setBackgroundColor(Color.TRANSPARENT);
			deleteButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View view) {
	            	removeFromMyEpisodes(show);
	            }
	        });
			
			final ImageButton infoButton = new ImageButton(getActivity());
			infoButton.setId(3);
			infoButton.setImageResource(R.drawable.down_arrow);
			infoButton.setPadding(3,6,3,6);
			infoButton.setBackgroundColor(Color.TRANSPARENT);
			
			final ScrollView scrollView = new ScrollView(getActivity());
			LinearLayout infoLayout = new LinearLayout(getActivity());
			infoLayout.setOrientation(LinearLayout.VERTICAL);
			final LinearLayout infoMain = new LinearLayout(getActivity());
			infoMain.setOrientation(LinearLayout.VERTICAL);
			
			List<Season> seasons = show.getSeasons();
			Collections.reverse(seasons);
			for(Season season : seasons) {
				TextView seasonbutton = new TextView(getActivity());
				seasonbutton.setText(getResources().getString(R.string.serie) + season.getSeasonNumber());
				seasonbutton.setGravity(Gravity.CENTER);
				seasonbutton.setTextSize(20);
				infoLayout.addView(seasonbutton);
				final TextView episodes = new TextView(getActivity());
				episodes.setVisibility(View.GONE);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				episodes.setLayoutParams(layoutParams);
				episodes.setGravity(Gravity.CENTER);
				episodes.setId(getNextId());
				episodes.setText("hÃ©r koma Ã¾Ã¦ttir");
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
	                    infoButton.setImageResource(R.drawable.down_arrow);
	                } else {
	                    animation = new Animator(infoMain, 500, 0);
	                    open.add(""+infoMain.getId());
	                    infoButton.setImageResource(R.drawable.up_arrow);
	                }
	                infoMain.startAnimation(animation);
				}
			});
			
			RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			RelativeLayout.LayoutParams calParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			RelativeLayout.LayoutParams delParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			//RelativeLayout.LayoutParams delParams = new RelativeLayout.LayoutParams(35, RelativeLayout.LayoutParams.WRAP_CONTENT);
			RelativeLayout.LayoutParams infoParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			
			titleParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			titleParams.addRule(RelativeLayout.CENTER_VERTICAL);
			calParams.addRule(RelativeLayout.LEFT_OF, 2);
			calParams.addRule(RelativeLayout.CENTER_VERTICAL);
			delParams.addRule(RelativeLayout.LEFT_OF, 3);
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
	}
	
	/**
     * Nafn: 	   Edda BjÃ¶rk KonrÃ¡Ã°sdÃ³ttir
     * Dagsetning: 30. oktÃ³ber 2014
     * Markmiï¿½:   Manual scroll view sem erfir frÃ¡ ScrollView svo hÃ¦gt sÃ©
     * 			  aÃ° virkja Ã¾aÃ° og ,,slÃ¶kkva Ã¡ Ã¾vÃ­'' Ã­ appinu
     * */
	private class MainScrollView extends ScrollView {
		private boolean scrollable = true;
		
		public MainScrollView(Context context) {
			super(context);
		}
		
		//Notkun: 		 scrollview.setScrollingEnabled(enabled)
		//EftirskilyrÃ°i: BÃºiÃ° er aÃ° virkja scrollview ef enabled er true
		//				 en ,,slÃ¶kkva Ã¡ Ã¾vÃ­'' annars
		public void setScrollingEnabled(boolean enabled) {
			scrollable = enabled;
		}
		
		//Notkun:		 isScrollable = scrollview.isScrollable()
		//EftirskilyrÃ°i: isScrollable er true ef scrollview er virkt,
		//				 false annars
		public boolean isScrollable() {
			return scrollable;
		}
		
		//Notkun: touch = scrollview.onTouchEvent(event)
		//EftirskilyrÃ°i:  touch er true ef scrollview er virkt og event
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
		//EftirskilyrÃ°i: interupt er false ef scrollview er ekki virkt, skilar
		//				 annars sama og samnefnt fall Ã­ ScrollView
		public boolean onInterceptTouchEvent(MotionEvent event) {
			if(!scrollable) return false;
			else return super.onInterceptTouchEvent(event);
		}
	}
	//Notkun:		 id = getNextId()
	//EftirskilyrÃ°i: id er nÃ¦sta lausa auÃ°kenni
	private int getNextId() {
		id = (id == null) ? 0 : id+1;
		return id;
	}
	
	//Notkun:		 bm = fixBitmapSize(orgBm);
  	//EftirskilyrÃ°i: bm er nÃ¦stum sama bitmap og orgBm nema Ã­ rÃ©ttri stÃ¦rÃ°
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