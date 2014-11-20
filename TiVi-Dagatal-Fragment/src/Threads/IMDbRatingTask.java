/**
 * Nafn: 		Kristin Fjola Tomasdottir
 * Dagsetning: 	20.november 2014
 * Markmid: 	IMDbRatingTask framkvaemir samhlida thradavinnu sem naer 
 * 				i IMDb einkunn fyrir thattarod
 */
package Threads;

import java.util.HashMap;
import java.util.Map;

import Clients.IMDbClient;
import Dtos.Show;
import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tivi_dagatal_fragment.FragmentList;
import com.example.tivi_dagatal_fragment.R;

public class IMDbRatingTask extends AsyncTask<Show, Integer, Show> {
	TextView textView;
	Activity context;
	//Notkun: task = IMDbRatingTask(textView, context)
	//Eftir:  task er nytt IMDbRatingTask
	public IMDbRatingTask(TextView textView, Activity context) {
		this.textView = textView;
		this.context = context;
	}
	
	// Notkun: map = doInBackground(maps)
	// Eftir:  map inniheldur thatt med einkunn og textasvaedi sem a ad birta einkunn i
	protected Show doInBackground(Show... shows) {
		IMDbClient.getIMDbRating(shows[0]);
		return shows[0];
	}
	
	// Notkun: onPostExecute(map)
	// Eftir:  einkunn fyrir thaddarod hefur verid birt
	protected void onPostExecute(Show show) {
		if(show.getImdbRating() != null) {
			textView.setText(context.getString(R.string.imdb_grade) + " " + show.getImdbRating());
		} else {
			textView.setText(context.getString(R.string.imdb_grade_not_found));
		}
	}
}
