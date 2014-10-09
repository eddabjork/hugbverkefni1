package Clients;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Dtos.Episode;
import Dtos.Show;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;


public class TraktClient {
	
	private String APIkey = "1b7308cb59d642b6548e8c8da531695b";
	private Date lastMonday = getLastMonday();
	private Date nextMonday = getNextMonday();
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
	private List<Show> searchShows = new ArrayList<Show>();
	private List<Episode> calendarEpisodes = new ArrayList<Episode>();
	private List<String> calendarSeasonsForShow = new ArrayList<String>();
	
	public TraktClient(){}
	
	public static Date getLastMonday(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		c = nullifyTime(c);
		Date lastMonday = c.getTime();
		format.format(lastMonday);
		
		return lastMonday;
	}
	
	public static Date getNextMonday(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		c.add(Calendar.DATE, 7);
		c = nullifyTime(c);
		Date nextMonday = c.getTime();
		format.format(nextMonday);
		
		return nextMonday;
	}
	
	public static Calendar nullifyTime(Calendar c){
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND,0);
		return c;
	}
	
	public List<Show> searchShow(final String title) {
			
		Thread thread = new Thread(new Runnable() {       	
        	@Override
            public void run() {
		        URL url = null;
		        try {
					url = new URL("http://api.trakt.tv/search/shows.json/" + APIkey + "?query=" + title.replaceAll("\\s+","+"));
				} catch (MalformedURLException e) {
					Log.e("URL error", "Could not make url for: " + title);
					e.printStackTrace();
				}
		        
		        try {
					final InputStream is = url.openStream();
					JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
					try {
						searchShows = readShowsArrayForSearch(reader);
					} finally {
						reader.close();
					}
				} catch (IOException e) {
					Log.e("API error", "Could not find show: " + title);
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
	
	public List<Show> readShowsArrayForSearch(JsonReader reader) throws IOException {
		List<Show> shows = new ArrayList<Show>();
		
		int count = 0;
		reader.beginArray();
		while (reader.hasNext()) {
			if(count > 20) {
				reader.skipValue();
			} else {
				shows.add(readShowForSearch(reader));
				count++;
			}
		}
		reader.endArray();
		return shows;
	}
 
	 public Show readShowForSearch(JsonReader reader) throws IOException {
		Show show = new Show();
		reader.beginObject();
		
		while (reader.hasNext()) {
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
	 
	 public List<Episode> getCalendarEpisodes(List<String> dataTitles){
		 for(final String dataTitle : dataTitles){	 		 
		 
			 // get the newest 2 seasons for the show
			 Thread thread1 = new Thread(new Runnable() {       	
	        	@Override
	            public void run() {
			        URL url = null;
			        try {
						url = new URL("http://api.trakt.tv/show/seasons.json/" + APIkey + "/" + dataTitle);
					} catch (MalformedURLException e) {
						Log.e("URL error", "Could not make url for: " + dataTitle);
						e.printStackTrace();
					}
			        
			        try {
						final InputStream is = url.openStream();
						JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
						try {
							calendarSeasonsForShow = readSeasonsArrayForCalendar(reader);
						} finally {
							reader.close();
						}
					} catch (IOException e) {
						Log.e("API error", "Could not find seasons for: " + dataTitle);
						e.printStackTrace();
					}
			        
	        	}
			});
			thread1.start();
			
			try {
				thread1.join();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
			
			for(final String season : calendarSeasonsForShow) {
		
				// get episodes for the newest 2 series
				Thread thread2 = new Thread(new Runnable() {       	
		        	@Override
		            public void run() {
				        URL url = null;
				        try {
							url = new URL("http://api.trakt.tv/show/season.json/" + APIkey + "/" + dataTitle + "/" + season);
						} catch (MalformedURLException e) {
							Log.e("URL error", "Could not make url for: " + dataTitle + " for season: " + season);
							e.printStackTrace();
						}
				        
				        try {
							final InputStream is = url.openStream();
							JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
							try {
								calendarEpisodes.addAll(readEpisodesArrayForCalendar(reader, dataTitle));
							} finally {
								reader.close();
							}
						} catch (IOException e) {
							Log.e("API error", "Could not find episodes for seasons: " + dataTitle + " season: " + season);
							e.printStackTrace();
						}
				        
		        	}
				});
				thread2.start();
				
				try {
					thread2.join();
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
			}
			
		 }		 
		 
		 return calendarEpisodes;
	 }
	 
	 public List<String> readSeasonsArrayForCalendar(JsonReader reader) throws IOException {
	    List<String> seasons = new ArrayList<String>();
	    int count = 0;
	    reader.beginArray();
	    while (reader.hasNext()) {	    	
	    	if(count >= 2) {
	    		reader.skipValue();
	    	} else {
	    		seasons.add(readSeasonForCalendar(reader));
	    		count++;
	    	}
	    }
	    reader.endArray();
	    return seasons;
	 }
	 
	 public String readSeasonForCalendar(JsonReader reader) throws IOException {
		reader.beginObject();
		String season = "";
		
		while (reader.hasNext()) {
		  String name = reader.nextName();
		  if (name.equals("season")) {
			  try {
				  season = Integer.toString(reader.nextInt());
			  } catch(Exception e){
				  reader.skipValue();
			  }
		  } else {
		    reader.skipValue();
		  }
		}
		reader.endObject();
		return season;
	  }
	 
	 public List<Episode> readEpisodesArrayForCalendar(JsonReader reader, String showDataTitle) throws IOException {
		List<Episode> episodes = new ArrayList<Episode>();
		
		reader.beginArray();
		while (reader.hasNext()) {
			Episode episode = readEpisodeForCalendar(reader, showDataTitle);
			if(episode != null) {
				episodes.add(episode);
			}
		}
		reader.endArray();
		return episodes;
	}
	 
	public Episode readEpisodeForCalendar(JsonReader reader, String showDataTitle) throws IOException {
		Episode episode = new Episode();
		reader.beginObject();
		
		boolean inTimePeriod = false;
		
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("title")) {
				try {
					episode.setTitle(reader.nextString());
				} catch(Exception e){
					reader.skipValue();
				}
			} else if (name.equals("season")) {
				try {
					episode.setSeason(reader.nextString());
				} catch(Exception e){
					reader.skipValue();
				}
			} else if (name.equals("episode")) {
				try {
					String episodeNr = reader.nextString();
					String dataTitle = showDataTitle + "/" + episode.getSeason() + "/" + episodeNr;
					episode.setDataTitle(dataTitle);
				} catch(Exception e){
					reader.skipValue();
				}
			} else if (name.equals("overview")) {
				try {
					episode.setOverview(reader.nextString());
				} catch(Exception e){
					reader.skipValue();
				}
			} else if (name.equals("first_aired_iso")) {
				try {
					String airDay = reader.nextString().substring(0,19);
					episode.setFirstAired(airDay);
					
					Date showDate = new Date();
					try {
			    		showDate = format.parse(airDay);
					} catch (ParseException e) {
						Log.e("date format", "Could not parse date for: " + showDate);
						e.printStackTrace();
					}
					
					if(showDate.after(lastMonday) && showDate.before(nextMonday)) {
						inTimePeriod = true;
					}
				} catch(Exception e){
					reader.skipValue();
				}
			} else if (name.equals("images")) {
				try {
					readImages(reader, episode);
				} catch(Exception e){
					reader.skipValue();
				}
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		if(inTimePeriod){
			return episode;
		}
		return null;
	}
	
	public void readImages(JsonReader reader, Episode episode) throws IOException{
		 reader.beginObject();
		 while(reader.hasNext()){
			 String name = reader.nextName();
			 if(name.equals("screen")){
				 try {
					 episode.setScreen(reader.nextString());
				 } catch(Exception e) {
					 reader.skipValue();
				 }
			 } else {
				 reader.skipValue();
			 }
		 }
		 reader.endObject();
	 }
		  
}
