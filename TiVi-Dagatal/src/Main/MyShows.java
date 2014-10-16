/*
 * Nafn: 		J�hanna Agnes Magn�sd�ttir
 * Dagsetning: 	9. okt�ber 2014
 * Markmi�: 	Stilla upp �tlit fyrir ��ttirnir-m�nir lista sem
 * 				inniheldur alla �� ��tti notandi hefur sett � tilsvarandi lista
 * 				(� gegnum search)
 */

package Main;

import java.io.InputStream;
import java.util.List;

import com.example.tivi_dagatal.R;
import com.example.tivi_dagatal.R.drawable;
import com.example.tivi_dagatal.R.id;
import com.example.tivi_dagatal.R.layout;
import com.example.tivi_dagatal.R.menu;
import com.example.tivi_dagatal.R.string;

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
		DbUtils dbHelper = new DbUtils(this);
		List<Show> showList = dbHelper.getAllShows();
		
		LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
    	ScrollView sv = new ScrollView(this);
    	LinearLayout ll = new LinearLayout(this);
    	ll.setOrientation(LinearLayout.VERTICAL);
    	
    	for(Show show : showList){
    		addShow(show, ll);
    	}
    	
    	sv.addView(ll);
	    setContentView(sv);	
    }
	
	//Notkun:		 addShow(show, mainLayout);
  	//Eftirskilyr�i: B�i� er a� b�a til view fyrir s�rhverja ��ttar��, sem samanstendur
	//				 af mynd ��ttara�ar, titil og �rj� takka (b�ta vi�/fjarl�gja �r dagatali,
	//				 uppl�singar og fjarl�gja). �etta view er svo b�tt vi� � a�al LinearLayout.
	public void addShow(final Show show, LinearLayout mainLayout) {		
		LinearLayout ll_1 = new LinearLayout(this);
		ll_1.setOrientation(LinearLayout.HORIZONTAL);
		
		ImageView image = new ImageView(this);
		image.setImageResource(R.drawable.ic_launcher);
		//enn�� veri� a� vinna � a� setja inn r�tta mynd.
		//new DownloadImageTask(image).execute("http://slurm.trakt.us/images/episodes/124-1-1.22.jpg");
		//image.buildDrawingCache()
		
		LinearLayout ll_2 = new LinearLayout(this);
		ll_2.setOrientation(LinearLayout.VERTICAL);
		
		TextView title = new TextView(this);
		title.setText(show.getTitle());
		
		LinearLayout ll_3 = new LinearLayout(this);
		ll_3.setOrientation(LinearLayout.HORIZONTAL);
		
		Button btn_cal = new Button(this);
		btn_cal.setText(getResources().getString(R.string.btn_cal));
		btn_cal.setTextSize(10);
		btn_cal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	addToCal(show);
            }
        });
		
		Button btn_info = new Button(this);
		btn_info.setText(getResources().getString(R.string.btn_info));
		btn_info.setTextSize(10);

		Button btn_delete = new Button(this);
		btn_delete.setText(getResources().getString(R.string.btn_delete));
		btn_delete.setTextSize(10);
		btn_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	removeFromMyEpisodes(show);
            }
        });

		ll_3.addView(btn_cal);
		ll_3.addView(btn_info);
		ll_3.addView(btn_delete);
		ll_2.addView(title);
		ll_2.addView(ll_3);
		ll_1.addView(image);
		ll_1.addView(ll_2);
		mainLayout.addView(ll_1);
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
