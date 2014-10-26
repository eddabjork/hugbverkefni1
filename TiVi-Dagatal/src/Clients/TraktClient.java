/**
 * Nafn: 		Krist�n Fj�la T�masd�ttir
 * Dagsetning: 	2. okt�ber 2014
 * Markmi�: 	Bi�ill sem n�r � uppl�singar um ��ttara�ir og ��tti fr� 
 * 				vef�j�nustu https://trakt.tv/. 
 */
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
import java.util.Map;

import Dtos.Episode;
import Dtos.Season;
import Dtos.Show;
import android.util.JsonReader;
import android.util.Log;


public class TraktClient {
	
	private String APIkey = "1b7308cb59d642b6548e8c8da531695b";
	private Date lastMonday = getLastMonday();
	private Date nextMonday = getNextMonday();
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	private List<Show> searchShows = new ArrayList<Show>();
	private List<Episode> calendarEpisodes = new ArrayList<Episode>();
	private List<String> calendarSeasonsForShow = new ArrayList<String>();
	private Show showInfo = new Show();
	private List<Season> seasonsForShowInfo = new ArrayList<Season>();
	private Episode episodeInfo = new Episode();
	
	public TraktClient(){}
	
	//Notkun: 		 date = getLastMonday()
	//Eftirskilyr�i: date er s��asli�inn m�nudagur
	public static Date getLastMonday(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		c = nullifyTime(c);
		Date lastMonday = c.getTime();
		format.format(lastMonday);
		
		return lastMonday;
	}
	
	//Notkun: 		 date = getNextMonday()
	//Eftirskilyr�i: date er n�stkomandi m�nudagur
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
	
	//Notkun: 		 calendar = nullifyTime(c)
	//Eftirskilyr�i: T�minn � deginum calendar hefur veri� settur � 00:00:00
	public static Calendar nullifyTime(Calendar c){
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND,0);
		return c;
	}
	
	//Notkun: 		 shows = searchShow(title)
	//Eftirskilyr�i: shows er listi af ��ttum sem eru ni�urst��ur �egar 
	//				 leita� er eftir strengnum title
	public List<Show> searchShow(final String title) {			
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
		
		return searchShows;	
	}
	
	//Notkun: 		 shows = readShowsArrayForSearch(reader)
	//Eftirskilyr�i: shows er listi af ��ttum sem eru ni�urst��ur �egar 
	//				 leita� er eftir strengnum title
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
 
	 //Notkun: 		  show = readShowForSearch(reader)
	 //Eftirskilyr�i: show er ��ttur sem er lesinn er �r staki
	 //				  �r json uppl�singum sem reader geymir	
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

	 //Notkun: 		  readImages(reader, show)
	 //Eftirskilyr�i: sl��ir � myndir fyrir show hafa veri� stilltar � show
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

	 //Notkun: 		  episodes = getCalendarEpisodes(dataTitles)
	 //Eftirskilyr�i: episodes er listi af ��ttum sem eiga a� vera
	 //				  birtir � dagatali
	 public List<Episode> getCalendarEpisodes(Map<String, String> dataTitles){
		 for(final String dataTitle : dataTitles.keySet()){
			 
			 getSeasonsForShow(dataTitle, false);
			 String showTitle = dataTitles.get(dataTitle);
			 
	        // get episodes for newest 2 seasons
			for(final String season : calendarSeasonsForShow) {		
		        URL url2 = null;
		        try {
					url2 = new URL("http://api.trakt.tv/show/season.json/" + APIkey + "/" + dataTitle + "/" + season);
				} catch (MalformedURLException e) {
					Log.e("URL error", "Could not make url for: " + dataTitle + " for season: " + season);
					e.printStackTrace();
				}
		        
		        try {
					final InputStream is = url2.openStream();
					JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
					try {
						calendarEpisodes.addAll(readEpisodesArrayForCalendar(reader, dataTitle, showTitle));
					} finally {
						reader.close();
					}
				} catch (IOException e) {
					Log.e("API error", "Could not find episodes for seasons: " + dataTitle + " season: " + season);
					e.printStackTrace();
				}
			}
			
		 }		 
		 
		 return calendarEpisodes;
	 }

	public void getSeasonsForShow(String dataTitle, boolean forInfo){
		// get the newest 2 seasons for the show
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
				if(forInfo){
					seasonsForShowInfo = readSeasonsArrayForShowInfo(reader);
				} else {
					calendarSeasonsForShow = readSeasonsArrayForCalendar(reader);
				}
			} finally {
				reader.close();
			}
		} catch (IOException e) {
			Log.e("API error", "Could not find seasons for: " + dataTitle);
			e.printStackTrace();
		}
	}

	 //Notkun: 		  seasons = readSeasonsArrayForCalendar(reader)
	 //Eftirskilyr�i: seasons er listi af n�merum � �eim ser�um sem � a� leita eftir
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
	 
	 //Notkun: 		  
	 //Eftirskilyr�i: 
	 public List<Season> readSeasonsArrayForShowInfo(JsonReader reader) throws IOException {
	    List<Season> seasons = new ArrayList<Season>();
	    reader.beginArray();
	    while (reader.hasNext()) {	 
	    	seasons.add(readSeasonForShowInfo(reader));
	    }
	    reader.endArray();
	    return seasons;
	 }
	 
	 //Notkun: 		  season = readSeasonForCalendar(reader)
	 //Eftirskilyr�i: season er n�mer � ser�u sem � a� leita eftir
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
	 
	 //Notkun: 		  
	 //Eftirskilyr�i:
	 public Season readSeasonForShowInfo(JsonReader reader) throws IOException {
		reader.beginObject();
		Season season = new Season();
		
		while (reader.hasNext()) {
		  String name = reader.nextName();
		  if (name.equals("season")) {
			  try {
				  season.setSeasonNumber(reader.nextInt());
			  } catch(Exception e){
				  reader.skipValue();
			  }
		  } else if (name.equals("episodes")) {
			  try {
				  season.setTotalEpisodes(reader.nextInt());
			  } catch(Exception e){
				  reader.skipValue();
			  }
		  } else if (name.equals("url")) {
			  try {
				  season.setUrl(reader.nextString());
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
	 
	 //Notkun: 		  episodes = readEpisodesArrayForCalendar(reader, showDataTitle)
	 //Eftirskilyr�i: episodes er listi af ��ttum sem eiga a� vera
	 //				  birtir � dagatali
	 public List<Episode> readEpisodesArrayForCalendar(JsonReader reader, String showDataTitle, String showTitle) throws IOException {
		List<Episode> episodes = new ArrayList<Episode>();
		
		reader.beginArray();
		while (reader.hasNext()) {
			Episode episode = readEpisodeForCalendar(reader, showDataTitle, showTitle);
			if(episode != null) {
				episodes.add(episode);
			}
		}
		reader.endArray();
		return episodes;
	}
	
	//Notkun: 		 episode = readEpisodeForCalendar(reader, showDataTitle)
	//Eftirskilyr�i: episode er ��ttur sem � a� vera birtur � dagatali
	public Episode readEpisodeForCalendar(JsonReader reader, String showDataTitle, String showTitle) throws IOException {
		Episode episode = new Episode();
		reader.beginObject();
		
		boolean inTimePeriod = false;
		
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("title")) {
				try {
					episode.setTitle(reader.nextString());
					episode.setShowTitle(showTitle);
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
					episode.setNumber(episodeNr);
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
	
	//Notkun: 		 readImages(reader, episode)
	//Eftirskilyr�i: sl��ir � myndir fyrir episode hafa veri� stilltar � episode
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
	
	public Show getShowInfo(Show show){
		URL url = null;
        try {
			url = new URL("http://api.trakt.tv/show/summary.json/" + APIkey + "/" + show.getDataTitle());
		} catch (MalformedURLException e) {
			Log.e("URL error", "Could not make url for: " + show.getTitle());
			e.printStackTrace();
		}
        
        try {
			final InputStream is = url.openStream();
			JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
			try {
				showInfo = readShowInfo(reader);
				getSeasonsForShow(show.getDataTitle(), true);
				showInfo.setSeasons(seasonsForShowInfo);
			} finally {
				reader.close();
			}
		} catch (IOException e) {
			Log.e("API error", "Could not find show: " + show.getTitle());
			e.printStackTrace();
		}
		return showInfo;
	}
	
	public Show readShowInfo(JsonReader reader) throws IOException {
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
		  } else if(name.equals("first_aired_iso")){
			  try {
				  show.setFirstAired(reader.nextString());
			  } catch(Exception e){
				  reader.skipValue();
			  }
		  } else if(name.equals("genres")){
			  readGenres(reader, show);
		  } else if(name.equals("images")){
			  readImages(reader, show);
		  } else {
		    reader.skipValue();
		  }
		}
		reader.endObject();
		return show;
	  }
	
	 //Notkun: 		  readGenres(reader, show)
	 //Eftirskilyr�i: 
	 public void readGenres(JsonReader reader, Show show) throws IOException{			
		reader.beginArray();
		List<String> genres = new ArrayList<String>();
		while (reader.hasNext()) {			
			genres.add(reader.nextString());
		}
		reader.endArray();
		show.setGenres(genres);
	 }
	 
	 //Notkun: 		  
	 //Eftirskilyr�i: 
	 public void getEpisodesForSeasonForShowInfo(Show show, Season season){
		List<Episode> episodes = new ArrayList<Episode>();
        URL url = null;
        try {
			url = new URL("http://api.trakt.tv/show/season.json/" + APIkey + "/" + show.getDataTitle() + "/" + season.getSeasonNumber());
		} catch (MalformedURLException e) {
			Log.e("URL error", "Could not make url for: " + show.getDataTitle() + " for season: " + season.getSeasonNumber());
			e.printStackTrace();
		}
        
        try {
			final InputStream is = url.openStream();
			JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
			try {
				episodes.addAll(readEpisodesArrayForCalendar(reader, show.getDataTitle(), show.getTitle()));
				season.getEpisodes().addAll(episodes);
			} finally {
				reader.close();
			}
		} catch (IOException e) {
			Log.e("API error", "Could not find episodes for season: " + show.getDataTitle() + " season: " + season.getSeasonNumber());
			e.printStackTrace();
		}
	 }
		  
}
