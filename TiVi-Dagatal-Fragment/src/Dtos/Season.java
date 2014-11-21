/**
 * Nafn:		Johanna Agnes Magnusdottir
 * Dagsetning:	23. oktober 2014
 * Markmid:		Klasinn er hlutur sem geymir upplysingar um thattaseriu.
 **/

package Dtos;

import java.util.List;

import android.widget.LinearLayout;

public class Season {
	private String title;
	private int seasonNumber;
	private int totalEpisodes;
	private String url;
	private String poster;
	private List<Episode> episodes;
	private LinearLayout episodesView;
	public Season(){}
	
	public Season(String title){
		this.title = title;
	}
	
	/*Getters*/
	
	//Notkun: seasonNumber = season.getSeasonNumber()
	//Eftir: seasonNumber er numer seriu season.
	public int getSeasonNumber() {
		return seasonNumber;
	}
	
	//Notkun: episodes = season.getTotalEpisodes()
	//Eftir: episodes er fjoldi thatta i season.
	public int getTotalEpisodes() {
		return totalEpisodes;
	}
		
	//Notkun: url = show.getUrl()
	//Eftir: url er slod a thattaseriu a trakt sidunni.
	public String getUrl() {
		return url;
	}
	
	//Notkun: poster = season.getPoster()
	//Eftir: poster er slod a forsidumynd thattaseriu.
	public String getPoster() {
		return poster;
	}
	
	//Notkun: episodes = season.getEpisodes()
	//Eftir: episodes eru allir thaettirnir i season.
	public List<Episode> getEpisodes() {
		return episodes;
	}
	
	//Notkun: episodesView = season.getEpisodesView()
	//Eftir: episodesView er EpisodesView fyrir seriuna season
	public LinearLayout getEpisodesView() {
		return this.episodesView;
	}
	
	/*Setters*/
	
	//Notkun: season.setSeasonNumber(seasonNumber)
	//Eftir: season er seriu numer seasonNumber.
	public void setSeasonNumber(int seasonNumber) {
		this.seasonNumber = seasonNumber;
	}
	
	//Notkun: season.setTotalEpisodes(totalEpisodes)
	//Eftir: season hefur totalEpisodes marga thaetti.
	public void setTotalEpisodes(int totalEpisodes) {
		this.totalEpisodes = totalEpisodes;
	}
	
	//Notkun: season.setUrl(url)
	//Eftir: url er slod a season hja trakt client.
	public void setUrl(String url) {
		this.url = url;
	}
	
	//Notkun: season.setPoster(poster)
	//Eftir: poster er url a adalmynd season.
	public void setPoster(String poster) {
		this.poster = poster;
	}
	
	//Notkun: season.setEpisodes(episodes)
	//Eftir: episodes eru allir thaettirnir i season.
	public void setEpisodes(List<Episode> episodes) {
		this.episodes = episodes;
	}
	
	//Notkun: season.setEpisodesView(episodesview)
	//Eftir: Buid er ad setja episodesview sem EpisodeView fyrir seriuna season
	public void setEpisodesView(LinearLayout episodesview) {
		this.episodesView = episodesview;
	}
}