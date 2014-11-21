/**
 * Nafn: 		Kristin Fjola Tomasdottir
 * Dagsetning: 	5. oktober 2014
 * Markmii: 	Bidill sem naer i upplysingar um thattaradir og thaetti fra 
 * 				vefthjonustu https://imdb.com/. 
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
	//Eftirskilyrdi: 	IMDb einkunn er stillt a show
	public static void getIMDbRating(final Show show){
		URL url = null;
        try {
			url = new URL("http://www.omdbapi.com/?i=" + show.getImdbId());
		} catch (MalformedURLException e) {
			Log.e("URL error", "Could not make url for: " + show.getTitle());
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
			Log.e("API error", "Could not find IMDb rating for show: " + show.getTitle());
			e.printStackTrace();
		}
	}
	
	//Notkun: 			readShow(reader, show)
	//Eftirskilyrdi: 	IMDb einkunn er stillt a show
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