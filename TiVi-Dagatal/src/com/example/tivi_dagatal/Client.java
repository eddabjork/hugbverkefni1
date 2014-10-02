package com.example.tivi_dagatal;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.util.JsonReader;
import android.util.Log;


public class Client {
	
	private String APIkey = "1b7308cb59d642b6548e8c8da531695b";
	
	public Client(){}
	
	public void searchShow(final String title) {
			
		Thread thread = new Thread(new Runnable() {       	
        	@Override
            public void run() {
		        URL url = null;
		        try {
					url = new URL("http://api.trakt.tv/search/shows.json/" + APIkey + "?query=" + title.replaceAll("\\s+","+"));
				} catch (MalformedURLException e) {
					Log.v("URL error", "Could not make url for: " + title);
					e.printStackTrace();
				}
		        
		        try {
					final InputStream is = url.openStream();
					JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
					try {
						List<Show> shows = readShowsArray(reader);
						for(Show show: shows){
							Log.v("title/year", show.getTitle() + "/" + show.getYear());
						}
					} finally {
						reader.close();
					}
				} catch (IOException e) {
					Log.v("API error", "Could not find show: " + title);
					e.printStackTrace();
				}
		        
        	}
		});
		thread.start();
		
	}
	
	 public List<Show> readShowsArray(JsonReader reader) throws IOException {
	    List<Show> shows = new ArrayList<Show>();
	
	    reader.beginArray();
	    while (reader.hasNext()) {
	    	shows.add(readShow(reader));
	    }
	    reader.endArray();
	    return shows;
	  }
	 
	 public Show readShow(JsonReader reader) throws IOException {
		Show show = new Show();
		
		reader.beginObject();
		while (reader.hasNext()) {
		  String name = reader.nextName();
		  if (name.equals("title")) {
		    show.setTitle(reader.nextString());
		  } else if (name.equals("year")) {
			  show.setYear(reader.nextString());
		  } else {
		    reader.skipValue();
		  }
		}
		reader.endObject();
		return show;
	  }

}
