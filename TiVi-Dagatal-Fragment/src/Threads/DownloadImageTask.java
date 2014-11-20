/**
 * Nafn: 		Kristin Fjola Tomasdottir
 * Dagsetning: 	20.november 2014
 * Markmid: 	Klasinn naer i myndir med samhlida thradavinnslu
 * */
package Threads;

import java.io.InputStream;

import Utils.VariousUtils;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;


public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	ImageView bmImage;
	Activity context;
	//Notkun: task = DownloadImageTask(bmImage, context)
	//Eftir:  task er nytt DownloadImageTask
	public DownloadImageTask(ImageView bmImage, Activity context) {
		this.bmImage = bmImage;
		this.context = context;
	}
	
	//Notkun:	bm = doInBackground(urls);
  	//Eftir: 	bm er myndin sem er sott fra urls.
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
	
	//Notkun:	onPostExecute(result);
  	//Eftir:	buid er ad setja myndina result a ImageView.
	protected void onPostExecute(Bitmap result) {
		bmImage.setImageBitmap(result);
		// set width of picture
		int width = VariousUtils.getScreenWidth(context);
		bmImage.buildDrawingCache();
		bmImage.setAdjustViewBounds(true);
		bmImage.getLayoutParams().width = width;
	}
}
