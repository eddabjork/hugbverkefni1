package com.example.tivi_dagatal;

import java.io.InputStream;

import Dtos.Episode;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;

public class MyEpisodesList extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_episodes_list);
		
		Episode episode = new Episode();
		episode.setTitle("Dance Moms");
		addShow(episode);
	}
	
	public void addShow(final Episode episode) {		
		LinearLayout mainLayout = (LinearLayout)findViewById(R.id.episode_layout);
		
		LinearLayout ll_1 = new LinearLayout(this);
		ll_1.setOrientation(LinearLayout.HORIZONTAL);
		
		ImageView image = new ImageView(this);
		//new DownloadImageTask(image).execute("http://slurm.trakt.us/images/episodes/124-1-1.22.jpg");
		image.setImageResource(R.drawable.ic_launcher);
		
		LinearLayout ll_2 = new LinearLayout(this);
		ll_2.setOrientation(LinearLayout.VERTICAL);
		
		TextView title = new TextView(this);
		title.setText(episode.getTitle());
		
		LinearLayout ll_3 = new LinearLayout(this);
		ll_3.setOrientation(LinearLayout.HORIZONTAL);
		
		Button btn_cal = new Button(this);
		btn_cal.setText("Setja � dagatal");
		btn_cal.setTextSize(10);
		btn_cal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	addToCal(episode);
            }
        });
		
		Button btn_info = new Button(this);
		btn_info.setText("Uppl�singar");
		btn_info.setTextSize(10);

		Button btn_delete = new Button(this);
		btn_delete.setText("Ey�a");
		btn_delete.setTextSize(10);

		ll_3.addView(btn_cal);
		ll_3.addView(btn_info);
		ll_3.addView(btn_delete);
		ll_2.addView(title);
		ll_2.addView(ll_3);
		ll_1.addView(image);
		ll_1.addView(ll_2);
		mainLayout.addView(ll_1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_episodes_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
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
			View rootView = inflater.inflate(R.layout.fragment_my_episodes_list,
					container, false);
			return rootView;
		}
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
			return mIcon11;
		}
		
		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}
	
	public void addToCal(Episode episode){
		//senda episode til Eddu
	}
}
