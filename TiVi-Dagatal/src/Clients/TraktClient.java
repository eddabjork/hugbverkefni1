package Clients;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.util.JsonReader;
import android.util.Log;

import com.example.tivi_dagatal.Show;

public class TraktClient {
	
	private String APIkey = "1b7308cb59d642b6548e8c8da531695b";
	
	private List<Show> searchShows = new ArrayList<Show>();
	
	public TraktClient(){}
	
	public List<Show> searchShow(final String title) {
			
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
						searchShows = readShowsArray(reader);
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
		
		try {
			thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		
		return searchShows;	
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
		int count = 0;
		reader.beginObject();
		
		while (reader.hasNext() && count < 20) {
		  String name = reader.nextName();
		  if (name.equals("title")) {
			  try {
				  show.setTitle(reader.nextString());
			  } catch(Exception e){
				  reader.skipValue();
			  }
		  } else if (name.equals("year")) {
			  try {
				  show.setYear(reader.nextString());
			  } catch(Exception e){
				  reader.skipValue();
			  }
		  } else if(name.equals("url")){
			  try {
				  String url = reader.nextString();
				  show.setUrl(url);
				  String dataTitle = url.substring(url.indexOf("show/") + 5,url.length());
				  show.setDataTitle(dataTitle);
			  } catch(Exception e){
				  reader.skipValue();
			  }
		  } else if(name.equals("overview")){
			  try {
				  show.setOverview(reader.nextString());
			  } catch(Exception e){
				  reader.skipValue();
			  }
		  } else if(name.equals("country")){
			  try {
				  show.setCountry(reader.nextString());
			  } catch(Exception e){
				  reader.skipValue();
			  }
		  } else if(name.equals("network")){
			  try {
				  show.setNetwork(reader.nextString());
			  } catch(Exception e){
				  reader.skipValue();
			  }
		  } else if(name.equals("air_day")){
			  try {
				  show.setAirDay(reader.nextString());
			  } catch(Exception e){
				  reader.skipValue();
			  }
		  } else if(name.equals("air_time")){
			  try {
				  show.setAirTime(reader.nextString());
			  } catch(Exception e){
				  reader.skipValue();
			  }
		  } else if(name.equals("imdb_id")){
			  try {
				  show.setImdbId(reader.nextString());
			  } catch(Exception e){
				  reader.skipValue();
			  }
		  } else if(name.equals("images")){
			  readImages(reader, show);
		  } else {
		    reader.skipValue();
		  }
		  count++;
		}
		reader.endObject();
		return show;
	  }
	 
	 public void readImages(JsonReader reader, Show show) throws IOException{
		 reader.beginObject();
		 while(reader.hasNext()){
			 String name = reader.nextName();
			 if(name.equals("poster")){
				 try {
					 show.setPoster(reader.nextString());
				 } catch(Exception e) {
					 
				 }
			 } else if(name.equals("fanart")){
				 try {
					 show.setFanart(reader.nextString());
				 } catch(Exception e) {
					 
				 }
			 } else if(name.equals("banner")){
				 try {
					 show.setBanner(reader.nextString());
				 } catch(Exception e) {
					 
				 }
			 } else {
				 reader.skipValue();
			 }
		 }
		 reader.endObject();
	 }
}
