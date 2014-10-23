/** Nafn: 		Jï¿½hanna Agnes Magnï¿½sdï¿½ttir
 * Dagsetning: 	9. oktï¿½ber 2014
 * Markmiï¿½: 	Stilla upp ï¿½tlit fyrir ï¿½ï¿½ttirnir-mï¿½nir lista sem
 * 				inniheldur alla ï¿½ï¿½ ï¿½ï¿½tti notandi hefur sett ï¿½ tilsvarandi lista
 * 				(ï¿½ gegnum search)
 */

package com.example.tivi_dagatal;
import Clients.TraktClient;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

<<<<<<< Updated upstream
=======
import com.example.tivi_dagatal.Animator;

import Clients.TraktClient;
>>>>>>> Stashed changes
import Data.DbUtils;
import Dtos.Show;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_shows);
		
		setLayout();
	}
	
	//Notkun:		 setLayout();
  	//Eftirskilyrï¿½i: Bï¿½iï¿½ er aï¿½ setja upp ï¿½tlit fyrir ï¿½ï¿½ttirnir-mï¿½nir listi, sem er LinearLayout
	//				 innan ï¿½ ScrollView, og viï¿½eigandi ï¿½ï¿½ttaraï¿½ir hafa veriï¿½ settir ï¿½ar inn.
	public void setLayout() {
		new GetAllShowsTask().execute();
    }
	
	//Notkun:		 addShow(show, mainLayout);
  	//Eftirskilyrï¿½i: Bï¿½iï¿½ er aï¿½ bï¿½a til view fyrir sï¿½rhverja ï¿½ï¿½ttarï¿½ï¿½, sem samanstendur
	//				 af mynd ï¿½ï¿½ttaraï¿½ar, titil og ï¿½rjï¿½ takka (bï¿½ta viï¿½/fjarlï¿½gja ï¿½r dagatali,
	//				 upplï¿½singar og fjarlï¿½gja). ï¿½etta view er svo bï¿½tt viï¿½ ï¿½ aï¿½al LinearLayout.
	public void addShow(final Show show, LinearLayout mainLayout) {		
		LinearLayout episodeLayout = new LinearLayout(this);
		episodeLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		ImageView image = getImage(show);
		LinearLayout titleButtonsLayout = getTitleButtonsLayout(show);
		
		episodeLayout.addView(image);
		episodeLayout.addView(titleButtonsLayout);
		mainLayout.addView(episodeLayout);
		mainLayout.addView(makeLine());
	}
	
	public ImageView getImage(Show show){
		ImageView image = new ImageView(this);
<<<<<<< Updated upstream
		//image.setImageResource(R.drawable.ic_launcher);
		String imgUrl = show.getPoster();
		new DownloadImageTask(image).execute(imgUrl);
		image.buildDrawingCache();
=======
		image.setImageResource(R.drawable.ic_launcher);
		//ennï¿½ï¿½ veriï¿½ aï¿½ vinna ï¿½ aï¿½ setja inn rï¿½tta mynd.
		//new DownloadImageTask(image).execute("http://slurm.trakt.us/images/episodes/124-1-1.22.jpg");
		//image.buildDrawingCache()
>>>>>>> Stashed changes
		
		//image.setLayoutParams(new FrameLayout.LayoutParams(100,100));
		//image.getLayoutParams().width = 100;
		
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
  	//Eftirskilyrði: titleButtonsLayout er layout sem inniheldur titil
	//				 þáttaraðar show og takka sem tengast honum
	public LinearLayout getTitleButtonsLayout(final Show show) {
		LinearLayout titleButtonsLayout = new LinearLayout(this);
		titleButtonsLayout.setOrientation(LinearLayout.VERTICAL);
		
		TextView title = new TextView(this);
		title.setText(show.getTitle());
		
		LinearLayout buttonsLayout = new LinearLayout(this);
		buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		Button calendarButton = getCalButton(show);
		
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
		return titleButtonsLayout;
	}
	
	//Notkun:		 calButton = getCalButton(show)
  	//Eftirskilyrði: calButton er takki sem stjórnar því hvort show
	//				 sé á dagatali
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
<<<<<<< Updated upstream
		return calendarButton;
=======
		
		Button infoButton = new Button(this);
		infoButton.setText(getResources().getString(R.string.btn_info));
		infoButton.setTextSize(10);
		final TextView info = new TextView(this);
		info.setText("her koma upplysingar");
		info.setVisibility(View.GONE);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		info.setLayoutParams(layoutParams);
		info.setGravity(Gravity.CENTER);
		info.setId(getNextId());
		Animator.setHeightForWrapContent(this, info);
		infoButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Animator animation = null;
                if(open.contains(""+info.getId())) {
                    animation = new Animator(info, 500, 1);
                    open.remove(""+info.getId());
                } else {
                    animation = new Animator(info, 500, 0);
                    open.add(""+info.getId());
                }
                info.startAnimation(animation);
			}
		});

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
		mainLayout.addView(info);
		mainLayout.addView(makeLine());
>>>>>>> Stashed changes
	}
	
	//Notkun:		 line = makeLine();
  	//Eftirskilyrï¿½i: line er nï¿½na view hlutur sem er einfï¿½ld, ï¿½unn, grï¿½ lï¿½na.
	public View makeLine(){
		 View v = new View(this);
		 v.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 3, (float) 0.80));
		 v.setBackgroundColor(Color.rgb(203,203,203));
		 return v;
	 }
	
	/*
     * Nafn: Kristï¿½n Fjï¿½la Tï¿½masdï¿½ttir
     * Dagsetning: 9. oktï¿½ber 2014
     * Markmiï¿½: Nï¿½ ï¿½ myndir meï¿½ samhliï¿½a ï¿½rï¿½ï¿½avinnslu
     * */
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;
		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}
		
		//Notkun:		 bm = doInBackground(urls);
	  	//Eftirskilyrï¿½i: bm er myndin sem er sï¿½tt frï¿½ urls.
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
	  	//Eftirskilyrï¿½i: bï¿½iï¿½ er aï¿½ setja myndina result ï¿½ rï¿½tt ImageView.
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
  	//Eftirskilyrï¿½i: Bï¿½iï¿½ er aï¿½ uppfï¿½ra gagnagrunn ï¿½.a. gildiï¿½ on_calendar=true fyrir show.
	public void addToCal(Show show){
		DbUtils dbHelper = new DbUtils(this);
		dbHelper.putShowOnCal(show);
	}
	
	//Notkun:		 remFromCal(show);
  	//Eftirskilyrï¿½i: Bï¿½iï¿½ er aï¿½ uppfï¿½ra gagnagrunn ï¿½.a. gildiï¿½ on_calendar=false fyrir show.
	public void remFromCal(Show show){
		DbUtils dbHelper = new DbUtils(this);
		dbHelper.takeShowOffCal(show);
	}
	
	//Notkun:		 removeFromMyEpisodes(show);
  	//Eftirskilyrï¿½i: Bï¿½iï¿½ er eyï¿½a ï¿½t show ï¿½r gagnagrunni.
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
  	//Eftirskilyrï¿½i: Upphafsskjï¿½r (sem inniheldur viku-dagatal) hefur opnast.
	public void onHome(View view){
    	Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
	
	private int getNextId() {
		id = (id == null) ? 0 : id+1;
		return id;
	}
	
	
	
}
