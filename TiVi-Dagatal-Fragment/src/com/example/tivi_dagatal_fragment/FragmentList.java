package com.example.tivi_dagatal_fragment;

import java.io.InputStream;
import java.util.List;

import Data.DbUtils;
import Dtos.Show;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

public class FragmentList extends Fragment {
	private ScrollView scrollView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cal, container, false);
		
		/****Til að testa
		DbUtils dbHelper = new DbUtils(getActivity());
		Show show1 = new Show();
        show1.setTitle("New Girl");
        show1.setDataTitle("new-girl");
        show1.setPoster("kallaposter");
        dbHelper.saveShow(show1);
		Show show2 = new Show();
        show2.setTitle("Big Bang Theory");
        show2.setDataTitle("big-bang-theory");
        show2.setPoster("kallaposter2");
        dbHelper.saveShow(show2);
        */
        
		scrollView = new ScrollView(getActivity());
		setLayout();
		view = scrollView;
        return view;
	}
	
	public void setLayout(){
		new GetAllShowsTask().execute();
	}
	
	private class GetAllShowsTask extends AsyncTask<Void, Integer, List<Show>> {
		protected List<Show> doInBackground(Void... voids) {
			DbUtils dbHelper = new DbUtils(getActivity());
			List<Show> showList = dbHelper.getAllShows();
			return showList;
		}
		
		protected void onProgressUpdate(Integer... progress) {
			//setProgressPercent(progress[0]);
		}
		
		protected void onPostExecute(List<Show> showList) {
	    	LinearLayout mainLayout = new LinearLayout(getActivity());
	    	mainLayout.setOrientation(LinearLayout.VERTICAL);
	    	
	    	for(Show show : showList){
	    		addShow(show, mainLayout);
	    	}
	    	
	    	scrollView.addView(mainLayout);
		}
	}
	
	public void addShow(final Show show, LinearLayout mainLayout){
		LinearLayout episodeLayout = new LinearLayout(getActivity());
		episodeLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		setTitleButtonLayout(show, episodeLayout);
		
		mainLayout.addView(episodeLayout);
		mainLayout.addView(makeLine());

	}
	
	public void setTitleButtonLayout(final Show show, LinearLayout episodeLayout){
		TextView title = new TextView(getActivity());
		title.setText(show.getTitle());
		
		Button calendarButton = getCalButton(show);
		
		Button deleteButton = new Button(getActivity());
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
	}
	
	public void remFromCal(Show show){
		DbUtils dbHelper = new DbUtils(getActivity());
		dbHelper.takeShowOffCal(show);
	}
	
	public void removeFromMyEpisodes(Show show){
		DbUtils dbHelper = new DbUtils(getActivity());
		dbHelper.deleteShow(show);
	}
	
	public View makeLine(){
		 View v = new View(getActivity());
		 v.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 1, (float) 0.80));
		 v.setBackgroundColor(Color.rgb(203,203,203));
		 return v;
	}
	
	public ImageView getImage(Show show){
		ImageView image = new ImageView(getActivity());
		image.setImageResource(R.drawable.ic_launcher);
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