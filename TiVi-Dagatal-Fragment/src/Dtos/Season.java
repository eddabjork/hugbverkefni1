/**
 * Nafn:		Jóhanna Agnes Magnúsdóttir
 * Dagsetning:	23. október 2014
 * Markmið:		Klasinn er hlutur sem geymir upplýsingar um þáttaseríu.
 **/

package Dtos;

import java.util.List;

public class Season {
	private String title;
	private int seasonNumber;
	private int totalEpisodes;
	private String url;
	private String poster;
	private List<Episode> episodes;
	
	public Season(){}
	
	public Season(String title){
		this.title = title;
	}
	
	/*Getters*/
	
	//Notkun: seasonNumber = season.getSeasonNumber()
	//Eftirskilyrði: seasonNumber er númer seríu season.
	public int getSeasonNumber() {
		return seasonNumber;
	}
	
	//Notkun: episodes = season.getTotalEpisodes()
	//Eftirskilyrði: episodes er fjöldi þátta í season.
	public int getTotalEpisodes() {
		return totalEpisodes;
	}
		
	//Notkun: url = show.getUrl()
	//Eftirskilyrði: url er slóð á þáttaseríu á trakt síðunni.
	public String getUrl() {
		return url;
	}
	
	//Notkun: poster = season.getPoster()
	//Eftirskilyrði: poster er slóð á forsíðumynd þáttaseríu.
	public String getPoster() {
		return poster;
	}
	
	//Notkun: episodes = season.getEpisodes()
	//Eftirskilyrði: episodes eru allir þættirnir í season.
	public List<Episode> getEpisodes() {
		return episodes;
	}
	
	/*Setters*/
	
	//Notkun: season.setSeasonNumber(seasonNumber)
	//Eftirskilyrði: season er sería númer seasonNumber.
	public void setSeasonNumber(int seasonNumber) {
		this.seasonNumber = seasonNumber;
	}
	
	//Notkun: season.setTotalEpisodes(totalEpisodes)
	//Eftirskilyrði: season hefur totalEpisodes marga þætti.
	public void setTotalEpisodes(int totalEpisodes) {
		this.totalEpisodes = totalEpisodes;
	}
	
	//Notkun: season.setUrl(url)
	//Eftirskilyrði: url er slóð á season í trakt client.
	public void setUrl(String url) {
		this.url = url;
	}
	
	//Notkun: season.setPoster(poster)
	//Eftirskilyrði: poster er url á aðalmynd season.
	public void setPoster(String poster) {
		this.poster = poster;
	}
	
	//Notkun: season.setEpisodes(episodes)
	//Eftirskilyrði: episodes eru allir þættirnir í season.
	public void setEpisodes(List<Episode> episodes) {
		this.episodes = episodes;
	}
}