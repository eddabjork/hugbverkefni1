/** Nafn: 		J�hanna Agnes Magn�sd�ttir
 * Dagsetning: 	9. okt�ber 2014
 * Markmi�: 	Stilla upp �tlit fyrir ��ttirnir-m�nir lista sem
 * 				inniheldur alla �� ��tti notandi hefur sett � tilsvarandi lista
 * 				(� gegnum search)
 */

package com.example.tivi_dagatal;
import Clients.TraktClient;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.tivi_dagatal.Animator;

import Clients.TraktClient;
import Data.DbUtils;
import Dtos.Season;
import Dtos.Show;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

public class MyShows extends ActionBarActivity {
	/** Saves current state and sets the view */
	private Integer id;
	private List<String> open = new ArrayList<String>();
	private LinearLayout mainLayout;
	private MainScrollView mainScrollView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_shows);
		
		setLayout();
	}
	
	//Notkun:		 setLayout();
  	//Eftirskilyr�i: B�i� er a� setja upp �tlit fyrir ��ttirnir-m�nir listi, sem er LinearLayout
	//				 innan � ScrollView, og vi�eigandi ��ttara�ir hafa veri� settir �ar inn.
	public void setLayout() {
		new GetAllShowsTask().execute();
    }
	
	//Notkun:		 addShow(show, mainLayout);
  	//Eftirskilyr�i: B�i� er a� b�a til view fyrir s�rhverja ��ttar��, sem samanstendur
	//				 af mynd ��ttara�ar, titil og �rj� takka (b�ta vi�/fjarl�gja �r dagatali,
	//				 uppl�singar og fjarl�gja). �etta view er svo b�tt vi� � a�al LinearLayout.
	public void addShow(final Show show) {	
		new ShowInfoTask().execute(show);
	}
	
	public ImageView getImage(Show show){
		ImageView image = new ImageView(this);
		image.setImageResource(R.drawable.ic_launcher);
		//String imgUrl = show.getPoster();
		//new DownloadImageTask(image).execute(imgUrl);
		//image.buildDrawingCache();
		
		return image;
	}
	
	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // CREATE A MATRIX FOR THE MANIPULATION
	    Matrix matrix = new Matrix();
	    // RESIZE THE BIT MAP
	    matrix.postScale(scaleWidth, scaleHeight);

	    // "RECREATE" THE NEW BITMAP
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	    return resizedBitmap;
	}
	
	//Notkun:		 titleButtonsLayout = getTitleButtonsLayout(show);
  	//Eftirskilyr�i: titleButtonsLayout er layout sem inniheldur titil
	//				 ��ttara�ar show og takka sem tengast honum
	public void setTitleButtonLayout(final Show show, LinearLayout episodeLayout) {
		TextView title = new TextView(this);
		title.setText(show.getTitle());
		
		Button calendarButton = getCalButton(show);
		
		Button deleteButton = new Button(this);
		deleteButton.setText(getResources().getString(R.string.btn_delete));
		deleteButton.setTextSize(10);
		deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	removeFromMyEpisodes(show);
            }
        });

		episodeLayout.addView(title);
		episodeLayout.addView(calendarButton);
		episodeLayout.addView(deleteButton);
	}
	
	//Notkun:		 calButton = getCalButton(show)
  	//Eftirskilyr�i: calButton er takki sem stj�rnar �v� hvort show
	//				 s� � dagatali
	public Button getCalButton(final Show show){
		final Button calendarButton = new Button(this);
		DbUtils dbHelper = new DbUtils(this);
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
	
	//Notkun:		 line = makeLine();
  	//Eftirskilyr�i: line er n�na view hlutur sem er einf�ld, �unn, gr� l�na.
	public View makeLine(){
		 View v = new View(this);
		 v.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 1, (float) 0.80));
		 v.setBackgroundColor(Color.rgb(203,203,203));
		 return v;
	 }
	
	/*
     * Nafn: Krist�n Fj�la T�masd�ttir
     * Dagsetning: 9. okt�ber 2014
     * Markmi�: N� � myndir me� samhli�a �r��avinnslu
     * */
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;
		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}
		
		//Notkun:		 bm = doInBackground(urls);
	  	//Eftirskilyr�i: bm er myndin sem er s�tt fr� urls.
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
	  	//Eftirskilyr�i: b�i� er a� setja myndina result � r�tt ImageView.
		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
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
	
	private class GetAllShowsTask extends AsyncTask<Void, Integer, List<Show>> {
		protected List<Show> doInBackground(Void... voids) {
			DbUtils dbHelper = new DbUtils(MyShows.this);
			List<Show> showList = dbHelper.getAllShows();
			return showList;
		}
		
		protected void onProgressUpdate(Integer... progress) {
			//setProgressPercent(progress[0]);
		}
		
		protected void onPostExecute(List<Show> showList) {
			LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			mainScrollView = new MainScrollView(MyShows.this);
	    	mainLayout = new LinearLayout(MyShows.this);
	    	mainLayout.setOrientation(LinearLayout.VERTICAL);
	    	
	    	for(Show show : showList){
	    		addShow(show);
	    	}
	    	
	    	mainScrollView.addView(mainLayout);
		    setContentView(mainScrollView);	
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
		
		protected void onPostExecute(Show show) {
			LinearLayout episodeLayout = new LinearLayout(MyShows.this);
			episodeLayout.setOrientation(LinearLayout.HORIZONTAL);
			
			setTitleButtonLayout(show,episodeLayout);
			
			Button dummy = new Button(MyShows.this);
			dummy.setText("uppl");
			
			final ScrollView scrollView = new ScrollView(MyShows.this);
			LinearLayout infoLayout = new LinearLayout(MyShows.this);
			infoLayout.setOrientation(LinearLayout.VERTICAL);
			final LinearLayout infoMain = new LinearLayout(MyShows.this);
			infoMain.setOrientation(LinearLayout.VERTICAL);
			
			List<Season> seasons = show.getSeasons();
			Collections.reverse(seasons);
			for(Season season : seasons) {
				TextView seasonbutton = new TextView(MyShows.this);
				seasonbutton.setText("Sería " + season.getSeasonNumber());
				seasonbutton.setGravity(Gravity.CENTER);
				seasonbutton.setTextSize(20);
				infoLayout.addView(seasonbutton);
				final TextView episodes = new TextView(MyShows.this);
				episodes.setVisibility(View.GONE);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				episodes.setLayoutParams(layoutParams);
				episodes.setGravity(Gravity.CENTER);
				episodes.setId(getNextId());
				episodes.setText("hér koma þættir");
				infoLayout.addView(episodes);
				Animator.setHeightForWrapContent(MyShows.this, episodes);
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
			Animator.setHeightForWrapContent(MyShows.this, infoMain);
			dummy.setOnClickListener(new View.OnClickListener() {
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
			
			episodeLayout.addView(dummy);
			mainLayout.addView(episodeLayout);
			mainLayout.addView(infoMain);
			mainLayout.addView(makeLine());
		}
	}
	
	//Notkun:		 addToCal(show);
  	//Eftirskilyr�i: B�i� er a� uppf�ra gagnagrunn �.a. gildi� on_calendar=true fyrir show.
	public void addToCal(Show show){
		DbUtils dbHelper = new DbUtils(this);
		dbHelper.putShowOnCal(show);
	}
	
	//Notkun:		 remFromCal(show);
  	//Eftirskilyr�i: B�i� er a� uppf�ra gagnagrunn �.a. gildi� on_calendar=false fyrir show.
	public void remFromCal(Show show){
		DbUtils dbHelper = new DbUtils(this);
		dbHelper.takeShowOffCal(show);
	}
	
	//Notkun:		 removeFromMyEpisodes(show);
  	//Eftirskilyr�i: B�i� er ey�a �t show �r gagnagrunni.
	public void removeFromMyEpisodes(Show show){
		DbUtils dbHelper = new DbUtils(this);
		dbHelper.deleteShow(show);
		finish();
		startActivity(getIntent());
	}

	/** Inflate the menu; this adds items to the action bar if it is present */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.my_episodes_list, menu);
		return true;
	}

	/** Handles presses on the action bar 
	 *  Parameter item: the item that was pressed on
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		View x = new View(this);
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.action_home){
			onHome(x);
			return true;
		}
		return super.onOptionsItemSelected(item);
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
	

	/**
     * A placeholder fragment containing a simple view.
     */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_my_shows,
					container, false);
			return rootView;
		}
	}
	
	//Notkun:		 onHome(view);
  	//Eftirskilyr�i: Upphafsskj�r (sem inniheldur viku-dagatal) hefur opnast.
	public void onHome(View view){
    	Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
	
	private int getNextId() {
		id = (id == null) ? 0 : id+1;
		return id;
	}
	
	
	
}
