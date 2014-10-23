/** Nafn: 		J�hanna Agnes Magn�sd�ttir
 * Dagsetning: 	9. okt�ber 2014
 * Markmi�: 	Stilla upp �tlit fyrir ��ttirnir-m�nir lista sem
 * 				inniheldur alla �� ��tti notandi hefur sett � tilsvarandi lista
 * 				(� gegnum search)
 */

package com.example.tivi_dagatal;
import java.io.InputStream;
import java.util.List;

import Clients.TraktClient;
import Data.DbUtils;
import Dtos.Show;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

public class MyShows extends ActionBarActivity {
	/** Saves current state and sets the view */
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
	public void addShow(final Show show, LinearLayout mainLayout) {		
		LinearLayout episodeLayout = new LinearLayout(this);
		episodeLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		ImageView image = new ImageView(this);
		image.setImageResource(R.drawable.ic_launcher);
		//enn�� veri� a� vinna � a� setja inn r�tta mynd.
		//new DownloadImageTask(image).execute("http://slurm.trakt.us/images/episodes/124-1-1.22.jpg");
		//image.buildDrawingCache()
		
		LinearLayout titleButtonsLayout = new LinearLayout(this);
		titleButtonsLayout.setOrientation(LinearLayout.VERTICAL);
		
		TextView title = new TextView(this);
		title.setText(show.getTitle());
		
		LinearLayout buttonsLayout = new LinearLayout(this);
		buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		Button calendarButton = new Button(this);
		calendarButton.setText(getResources().getString(R.string.btn_cal));
		calendarButton.setTextSize(10);
		calendarButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	addToCal(show);
            }
        });
		
		Button infoButton = new Button(this);
		infoButton.setText(getResources().getString(R.string.btn_info));
		infoButton.setTextSize(10);

		Button deleteButton = new Button(this);
		deleteButton.setText(getResources().getString(R.string.btn_delete));
		deleteButton.setTextSize(10);
		deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	removeFromMyEpisodes(show);
            }
        });

		buttonsLayout.addView(calendarButton);
		buttonsLayout.addView(infoButton);
		buttonsLayout.addView(deleteButton);
		titleButtonsLayout.addView(title);
		titleButtonsLayout.addView(buttonsLayout);
		episodeLayout.addView(image);
		episodeLayout.addView(titleButtonsLayout);
		mainLayout.addView(episodeLayout);
		mainLayout.addView(makeLine());
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
			return mIcon11;
		}
		
		//Notkun:		 onPostExecute(result);
	  	//Eftirskilyr�i: b�i� er a� setja myndina result � r�tt ImageView.
		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
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
	    	ScrollView scrollView = new ScrollView(MyShows.this);
	    	LinearLayout mainLayout = new LinearLayout(MyShows.this);
	    	mainLayout.setOrientation(LinearLayout.VERTICAL);
	    	
	    	for(Show show : showList){
	    		addShow(show, mainLayout);
	    	}
	    	
	    	scrollView.addView(mainLayout);
		    setContentView(scrollView);	
		}
	}
	
	//Notkun:		 addToCal(show);
  	//Eftirskilyr�i: B�i� er a� uppf�ra gagnagrunn �.a. gildi� on_calendar=true fyrir show.
	public void addToCal(Show show){
		DbUtils dbHelper = new DbUtils(this);
		dbHelper.putShowOnCal(show);
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
	
	
	
}
