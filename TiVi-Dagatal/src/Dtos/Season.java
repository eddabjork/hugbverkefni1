/**
 * Nafn:		Jóhanna Agnes Magnúsdóttir
 * Dagsetning:	23. október 2014
 * Markmið:		Klasinn er hlutur sem geymir upplýsingar um þáttaseríu.
 **/

package Dtos;

public class Season {
	private String title;
	private int seasonNumber;
	private int episodes;
	private String url;
	private String poster;
	
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
	
	//Notkun: episodes = season.getEpisodes()
	//Eftirskilyrði: episodes er fjöldi þátta í season.
	public int getEpisodes() {
		return episodes;
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
	
	/*Setters*/
	
	//Notkun: season.setSeasonNumber(seasonNumber)
	//Eftirskilyrði: season er sería númer seasonNumber.
	public void setSeasonNumber(int seasonNumber) {
		this.seasonNumber = seasonNumber;
	}
	
	//Notkun: season.setEpisodes(episodes)
	//Eftirskilyrði: season hefur episodes marga þætti.
	public void setEpisodes(int episodes) {
		this.episodes = episodes;
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
}