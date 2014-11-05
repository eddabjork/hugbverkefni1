/**
 * Nafn: 		Kristín Fjóla Tómasdóttir
 * Dagsetning: 	5. október 2014
 * Markmið: 	Biðill sem nær í upplýsingar um þáttaraðir og þætti frá 
 * 				vefþjónustu https://imdb.com/. 
 */

package Clients;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import Dtos.Show;
import android.util.JsonReader;
import android.util.Log;


public class IMDbClient {
	
	//Notkun: 			getIMDbRating(show)
	//Eftirskilyrði: 	IMDb einkunn er stillt á show
	public static void getIMDbRating(final Show show){
		
	        URL url = null;
	        try {
				url = new URL("http://www.omdbapi.com/?i=" + show.getImdbId());
			} catch (MalformedURLException e) {
				Log.v("URL error", "Could not make url for: " + show.getTitle());
				e.printStackTrace();
			}
	        
	        try {
				final InputStream is = url.openStream();
				JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
				try {
					readShow(reader, show);
				} finally {
					reader.close();
				}
			} catch (IOException e) {
				Log.v("API error", "Could not find IMDb rating for show: " + show.getTitle());
				e.printStackTrace();
			}
	}
	
	//Notkun: 			readShow(reader, show)
	//Eftirskilyrði: 	IMDb einkunn er stillt á show
	public static void readShow(JsonReader reader, Show show) throws IOException {
		reader.beginObject();
		
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("imdbRating")) {
				try {
					show.setImdbRating(reader.nextString());
				} catch(Exception e){
					reader.skipValue();
				}
			} else {
				reader.skipValue();
			}
			
		}
		reader.endObject();
	}
}