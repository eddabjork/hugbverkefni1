/**
 * Nafn: 		Steinunn Fri�geirsd�ttir
 * Dagsetning: 	30. okt�ber 2014
 * Markmi�: 	FragmentEpisopde er fragment sem birtir uppl�singar
 * 				um einstaka ��tti.
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
import Utils.LayoutUtils;
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
	//Eftir: B�i� a� birta uppl�singar um einstaka ��tt
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_episode, container, false);
		scrollView = new ScrollView(getActivity());
		new ShowEpisodeTask().execute(episode);
        return view;
	}
	
	//Notkun: setEpisode(episode)  
	//Eftir: b�i� er a� setja episode sem gildi tilviksbreytinnar episode
	public void setEpisode(Episode episode){
		this.episode = episode;
	}	
	
	/**
     * Nafn: 		Steinunn Fri�geirsd�ttir
     * Dagsetning: 	6. november 2014
     * Markmi�: 	ShowEpisodeTask er klasi sem s�r um �r��avinnslu fyrir FragmentEpisode.
     * 				Uppl�singum um ��tt er hla�i� inn asyncronus me� loading skj�.
     */   
	private class ShowEpisodeTask extends AsyncTask<Object, Integer, Episode> {
		//Notkun: doInBackground(queries)
		//Eftir:  ���avinnslu � bakgrunni er loki�
		//        og slila� hefur veri� r�ttum episode.
		protected Episode doInBackground(Object... objects) {         
			IMDbClient imdb = new IMDbClient();
			Episode episode = (Episode)objects[0];
			return episode;
		}
		
		// Notkun: onPreExecute()
		// Eftir:  progressDialog hefur veri� stillt sem � a� s�na � me�an notandi er a� b��a
		protected void onPreExecute() {  
			progressDialog = LayoutUtils.showProgressDialog(R.string.popular_process_title, 
    				R.string.popular_process_msg, getActivity());	
        }  
		
		//Engin virkni
		protected void onProgressUpdate(Integer... progress) {
			//setProgressPercent(progress[0]);
		}
		
		//Notkun: onPostExecute(searchShows)
		//Eftir:  b�i� a� er hla�a inn �llum uppl�singum um episode og birta 
		//		  �samt �v� a� stoppa progressDialog
		protected void onPostExecute(Episode episode) {
			progressDialog.dismiss();
								
			ImageView image = (ImageView) getView().findViewById(R.id.image);
			String imgUrl = episode.getScreen();
			new DownloadImageTask(image).execute(imgUrl);
			
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
			else text = "Vantar n�mer ��ttar.";
			episodeNumber.setText(text);
			
			Date date = null;
			TextView airTime = (TextView) getView().findViewById(R.id.airTime);
			try {
				date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(episode.getFirstAired());
				String newDate = new SimpleDateFormat("HH:mm").format(date);
				bool = checkText(newDate);
				if(bool) text = newDate;
				else text = "Vantar s�ningart�ma.";
				airTime.setText(text);
			} catch (ParseException e) {
				e.printStackTrace();
				airTime.setText("S�ndur kl.: Vantar s�ningart�ma");
			}
			
			TextView firstAired = (TextView) getView().findViewById(R.id.firstAired);
			try {
				date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(episode.getFirstAired());
				String newDate = new SimpleDateFormat("dd.MM.yyyy").format(date);
				String time = new SimpleDateFormat("HH:mm").format(date);
				bool = checkText(newDate);
				if(bool) text = newDate;
				else text = "Vantar dagsetningu.";
				firstAired.setText(text);
			} catch (ParseException e) {
				e.printStackTrace();
				firstAired.setText("Fyrst s�ndur: Vantar dagsetningu");
			}
			
			TextView plot = (TextView) getView().findViewById(R.id.plot);
			bool = checkText(episode.getOverview());
			if(bool) text = episode.getOverview();
			else text = "S�gu�r��ur:\n Vantar l�singu.";
			plot.setText(text);  
		}
		
		protected boolean checkText (String string) {
			if(string == null || string.toString().isEmpty() || string.equals("TBA"))
				return false;
			return true;
		}
	}
	
	/**
     * Nafn: Krist�n Fj�la T�masd�ttir
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
			return mIcon11;
		}
		
		//Notkun:		 onPostExecute(result);
	  	//EftirskilyrÃ°i: bÃºiÃ° er aÃ° setja myndina result Ã­ rÃ©tt ImageView.
		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
			
			// set width of picture
			WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int width = size.x;
			bmImage.buildDrawingCache();
			bmImage.setAdjustViewBounds(true);
			bmImage.getLayoutParams().width = width;
		}
	}
	
	// Notkun: showDialog(show)
	// Eftir:  pop-up hefur veri� birt sem b��ur upp� a� vista show � dagatali 
	void showDialog(Show show) {
	    DialogFragment newFragment = PopUpPutOnCal.newInstance(show);
	    newFragment.show(getFragmentManager(), "dialog");
	}
}	

