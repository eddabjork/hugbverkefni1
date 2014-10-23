/**
 * Nafn:		J�hanna Agnes Magn�sd�ttir
 * Dagsetning:	23. okt�ber 2014
 * Markmi�:		Klasinn er hlutur sem geymir uppl�singar um ��ttaser�u.
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
	//Eftirskilyr�i: seasonNumber er n�mer ser�u season.
	public int getSeasonNumber() {
		return seasonNumber;
	}
	
	//Notkun: episodes = season.getEpisodes()
	//Eftirskilyr�i: episodes er fj�ldi ��tta � season.
	public int getEpisodes() {
		return episodes;
	}
		
	//Notkun: url = show.getUrl()
	//Eftirskilyr�i: url er sl�� � ��ttaser�u � trakt s��unni.
	public String getUrl() {
		return url;
	}
	
	//Notkun: poster = season.getPoster()
	//Eftirskilyr�i: poster er sl�� � fors��umynd ��ttaser�u.
	public String getPoster() {
		return poster;
	}
	
	/*Setters*/
	
	//Notkun: season.setSeasonNumber(seasonNumber)
	//Eftirskilyr�i: season er ser�a n�mer seasonNumber.
	public void setSeasonNumber(int seasonNumber) {
		this.seasonNumber = seasonNumber;
	}
	
	//Notkun: season.setEpisodes(episodes)
	//Eftirskilyr�i: season hefur episodes marga ��tti.
	public void setEpisodes(int episodes) {
		this.episodes = episodes;
	}
	
	//Notkun: season.setUrl(url)
	//Eftirskilyr�i: url er sl�� � season � trakt client.
	public void setUrl(String url) {
		this.url = url;
	}
	
	//Notkun: season.setPoster(poster)
	//Eftirskilyr�i: poster er url � a�almynd season.
	public void setPoster(String poster) {
		this.poster = poster;
	}
}