/**
 * Nafn: 		Steinunn Friðgeirsdóttir
 * Dagsetning: 	30. október 2014
 * Markmið: 	FragmentEpisopde er fragment sem birtir upplýsingar
 * 				um einstaka þætti.
 */
package com.example.tivi_dagatal_fragment;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Clients.IMDbClient;
import Dtos.Episode;
import Dtos.Show;
import android.app.ActionBar.LayoutParams;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ScrollView;
import android.widget.TextView;

public class FragmentEpisode extends Fragment{
	private ScrollView scrollView;
	private Episode episode;
	private ProgressDialog progressDialog;
	
	@Override
	//Eftir: Búið að birta upplýsingar um einstaka þátt
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_episode, container, false);
		scrollView = new ScrollView(getActivity());
		new ShowEpisodeTask().execute(episode);
		//view = scrollView;
		Log.v("er að búa til ", "episode");
        return view;
	}
	
	//Notkun: setEpisode(episode)  
	//Eftir: búið er að setja episode sem gildi tilviksbreytinnar episode
	public void setEpisode(Episode episode){
		this.episode = episode;
		Log.v("Var að sækja episode", episode.getTitle());
	}	
	
	/**
     * Nafn: 		Steinunn Friðgeirsdóttir
     * Dagsetning: 	6. november 2014
     * Markmið: 	ShowEpisodeTask er klasi sem sér um þráðavinnslu fyrir FragmentEpisode.
     * 				Upplýsingum um þátt er hlaðið inn asyncronus með loading skjá.
     */   
	private class ShowEpisodeTask extends AsyncTask<Object, Integer, Episode> {
		//Notkun: doInBackground(queries)
		//Eftir:  þáðavinnslu í bakgrunni er lokið
		//        og slilað hefur verið réttum episode.
		protected Episode doInBackground(Object... objects) {         
			IMDbClient imdb = new IMDbClient();
			Episode episode = (Episode)objects[0];
			return episode;
		}
		
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur verið stillt sem á að sýna á meðan notandi er að bíða
		protected void onPreExecute() {  
            progressDialog = new ProgressDialog(getActivity(), R.style.ProgressDialog);
            progressDialog.setTitle(getResources().getString(R.string.popular_process_title));  
            progressDialog.setMessage(getResources().getString(R.string.popular_process_msg)); 
            progressDialog.setCancelable(false);  
            progressDialog.setIndeterminate(false);  
            progressDialog.show();  
            // change color of divider
            int dividerId = progressDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
    		View divider = progressDialog.findViewById(dividerId);
    		divider.setBackgroundColor(getResources().getColor(R.color.app_red));
        }  
		
		//Engin virkni
		protected void onProgressUpdate(Integer... progress) {
			//setProgressPercent(progress[0]);
		}
		
		//Notkun: onPostExecute(searchShows)
		//Eftir:  búið að er hlaða inn öllum upplýsingum um episode og birta 
		//		  ásamt því að stoppa progressDialog
		protected void onPostExecute(Episode episode) {
			progressDialog.dismiss();
			
			WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int width = size.x;
								
			ImageView image = (ImageView) getView().findViewById(R.id.image);
			String imgUrl = episode.getScreen();
			new DownloadImageTask(image).execute(imgUrl);
			image.buildDrawingCache();
			image.setAdjustViewBounds(true);
			image.getLayoutParams().width = width;
			
			boolean bool = false;
			boolean bool2 = false;
			String text = null;

			TextView title = (TextView) getView().findViewById(R.id.title);
			bool = checkText(episode.getTitle());
			if(bool) text = episode.getTitle();
			else text = "Vantar titil.";
			title.setText(text);

			DecimalFormat formatter = new DecimalFormat("00");
			String season = formatter.format(Integer.parseInt(episode.getSeason()));
			String number = formatter.format(Integer.parseInt(episode.getNumber()));
			TextView episodeNumber = (TextView) getView().findViewById(R.id.episodeNumber);
			bool = checkText(season);
			bool2 = checkText(number);
			if (bool && bool2) text = "s"+season+"e"+number;
			else text = "Vantar númer þáttar.";
			episodeNumber.setText(text);
			
			Date date = null;
			TextView airTime = (TextView) getView().findViewById(R.id.airTime);
			try {
				date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(episode.getFirstAired());
				String newDate = new SimpleDateFormat("HH:mm").format(date);
				bool = checkText(newDate);
				if(bool) text = newDate;
				else text = "Vantar sýningartíma.";
				airTime.setText(text);
			} catch (ParseException e) {
				e.printStackTrace();
				airTime.setText("Sýndur kl.: Vantar sýningartíma");
			}
			
			TextView firstAired = (TextView) getView().findViewById(R.id.firstAired);
			try {
				date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(episode.getFirstAired());
				String newDate = new SimpleDateFormat("dd.MM.yyyy").format(date);
				String time = new SimpleDateFormat("HH:mm").format(date);
				Log.v("time", time);
				bool = checkText(newDate);
				if(bool) text = newDate;
				else text = "Vantar dagsetningu.";
				firstAired.setText(text);
			} catch (ParseException e) {
				e.printStackTrace();
				firstAired.setText("Fyrst sýndur: Vantar dagsetningu");
			}
			
			TextView plot = (TextView) getView().findViewById(R.id.plot);
			bool = checkText(episode.getOverview());
			if(bool) text = episode.getOverview();
			else text = "Söguþráður:\n Vantar lýsingu.";
			plot.setText(text);  
		}
		
		protected boolean checkText (String string) {
			if(string == null || string.toString().isEmpty() || string.equals("TBA"))
				return false;
			return true;
		}
	}
	
	/**
     * Nafn: Kristín Fjóla Tómasdóttir
     * Dagsetning: 9. oktÃƒÂ³ber 2014
     * MarkmiÃƒÂ°: NÃƒÂ¦r ÃƒÂ­ myndir meÃƒÂ° samhliÃƒÂ°a ÃƒÂ¾rÃƒÂ¡ÃƒÂ°avinnslu
     * */
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;
		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}
		
		//Notkun:		 bm = doInBackground(urls);
	  	//EftirskilyrÃƒÂ°i: bm er myndin sem er sÃƒÂ³tt frÃƒÂ¡ urls.
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
	  	//EftirskilyrÃƒÂ°i: bÃƒÂºiÃƒÂ° er aÃƒÂ° setja myndina result ÃƒÂ­ rÃƒÂ©tt ImageView.
		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}
	
	// Notkun: showDialog(show)
	// Eftir:  pop-up hefur verið birt sem býður uppá að vista show á dagatali 
	void showDialog(Show show) {
	    DialogFragment newFragment = PopUpPutOnCal.newInstance(show);
	    newFragment.show(getFragmentManager(), "dialog");
	}
}	

