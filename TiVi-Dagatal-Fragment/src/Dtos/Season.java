/**
 * Nafn:		J�hanna Agnes Magn�sd�ttir
 * Dagsetning:	23. okt�ber 2014
 * Markmi�:		Klasinn er hlutur sem geymir uppl�singar um ��ttaser�u.
 **/

package Dtos;

import java.util.List;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageButton;

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
	//Eftirskilyr�i: seasonNumber er n�mer ser�u season.
	public int getSeasonNumber() {
		return seasonNumber;
	}
	
	//Notkun: episodes = season.getTotalEpisodes()
	//Eftirskilyr�i: episodes er fj�ldi ��tta � season.
	public int getTotalEpisodes() {
		return totalEpisodes;
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
	
	//Notkun: episodes = season.getEpisodes()
	//Eftirskilyr�i: episodes eru allir ��ttirnir � season.
	public List<Episode> getEpisodes() {
		return episodes;
	}
	
	/*Setters*/
	
	//Notkun: season.setSeasonNumber(seasonNumber)
	//Eftirskilyr�i: season er ser�a n�mer seasonNumber.
	public void setSeasonNumber(int seasonNumber) {
		this.seasonNumber = seasonNumber;
	}
	
	//Notkun: season.setTotalEpisodes(totalEpisodes)
	//Eftirskilyr�i: season hefur totalEpisodes marga ��tti.
	public void setTotalEpisodes(int totalEpisodes) {
		this.totalEpisodes = totalEpisodes;
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
	
	//Notkun: season.setEpisodes(episodes)
	//Eftirskilyr�i: episodes eru allir ��ttirnir � season.
	public void setEpisodes(List<Episode> episodes) {
		this.episodes = episodes;
	}
	
	public void setEpisodesView(LinearLayout episodesview) {
		this.episodesView = episodesview;
	}
	
	public LinearLayout getEpisodesView() {
		return this.episodesView;
	}
}