/**
 * Nafn:		Steinunn Fridgeirsdottir
 * Dagsetning:	2. oktober 2014
 * Markmid:		Klasinn er hlutur sem geymir upplysingar um stakan thatt.
 **/
package Dtos;

import android.widget.ImageButton;
import android.widget.LinearLayout;

public class Episode {
	private String title;
	private String dataTitle;
	private String season;
	private String number;
	private String tvdbId;
	private String imdbId;
	private String overview;
	private String url;
	private String firstAired;
	private String screen;
	private String ratingPercentage;
	private String showTitle;
	private LinearLayout episodesView;
	private ImageButton infoButton;
	
	public Episode(){}
	
	public Episode(String title){
		this.title = title;
	}
	
	/*Getters*/
	
	//Notkun: first = episode.getFirstAired()
	//Eftir: first er frumsyningardagur episode.
	public String getFirstAired() {
		return firstAired;
	}
	
	//Notkun: imdbId = episode.getImdbId()
	//Eftir: imdbId er IMDb audkenni episode.
	public String getImdbId() {
		return imdbId;
	}
		
	//Notkun: number = episode.getNumber()
	//Eftir: number er thattanumer episode.
	public String getNumber() {
		return number;
	}
		
	//Notkun: overview = episode.getOverview()
	//Eftir: overview er lysing a episode.
	public String getOverview() {
		return overview;
	}
		
	//Notkun: ratingPer = episode.getRatingPercentage()
	//Eftir: ratingPer er hlutfall einkunna episode.
	public String getRatingPercentage() {
		return ratingPercentage;
	}
		
	//Notkun: screen = episode.getScreen()
	//Eftir: screen er mynd fyrir episode.
	public String getScreen() {
		return screen;
	}
		
	//Notkun: season = episode.getSeason()
	//Eftir: season er numer theirrar seriu sem episode er i.
	public String getSeason() {
		return season;
	}
		
	//Notkun: title = episode.getTitle()
	//Eftir: title er titill episode.
	public String getTitle() {
		return title;
	}
		
	//Notkun: id = episode.getTvdbId()
	//Eftir: id er Tvdb audkenni episode.
	public String getTvdbId() {
		return tvdbId;
	}
		
	//Notkun: ratingPer = episode.getRatingPercentage()
	//Eftir: ratingPer er hlutfall einkunna episode.
	public String getUrl() {
		return url;
	}
		
	//Notkun: dataTitle = episode.getDataTitle()
	//Eftir: dataTitle er trakt audkenni episode.
	public String getDataTitle(){
		return dataTitle;
	}
	
	//Notkun: showTitle = episode.getShowTitle()
	//Eftir: showTitle er titill thattaradarinnar sem episode tilheyrir.
	public String getShowTitle(){
		return showTitle;
	}
	
	//Notkun episodesView = episode.getEpisodesView()
	//Eftir: episodesView er EpisodeView fyrir thattinn episode
	public LinearLayout getEpisodesView() {
		return this.episodesView;
	}
	
	//Notkun: infoButton = episode.getInfoButton()
	//Eftir: infoButton er takki fyrir upplysingar fyrir thattinn episode
	public ImageButton getInfoButton() {
		return this.infoButton;
	}
	
	
	/*Setters*/
	
	//Notkun: episode.setFirstAired(firstAired)
	//Eftir: firstAired er frumsyningardagur episode.
	public void setFirstAired(String firstAired) {
		this.firstAired = firstAired;
	}
	
	//Notkun: episode.setImdbId(id)
	//Eftir: id er IMDb audkenni episode.
	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}
	
	//Notkun: episode.setNumber(number)
	//Eftir: number er numer episode i seriu.
	public void setNumber(String number) {
		this.number = number;
	}
	
	//Notkun: episode.setOverview(overview)
	//Eftir: overview er lysing a episode.
	public void setOverview(String overview) {
		this.overview = overview;
	}
	
	//Notkun: episode.setRatingPercentage(rat)
	//Eftir: rat er hlutfall einkunna a episode.
	public void setRatingPercentage(String ratingPercentage) {
		this.ratingPercentage = ratingPercentage;
	}
	
	//Notkun: episode.setScreen(screen)
	//Eftir: screen er mynd fyrir episode.
	public void setScreen(String screen) {
		this.screen = screen;
	}
	
	//Notkun: episode.setSeason(season)
	//Eftir: season er numer seriu sem episode er i.
	public void setSeason(String season) {
		this.season = season;
	}
	
	//Notkun: episode.setTitle(title)
	//Eftir: title er titill episode.
	public void setTitle(String title) {
		this.title = title;
	}
	
	//Notkun: episode.setTvdbId(id)
	//Eftir: id er Tvdb audkenni episode.
	public void setTvdbId(String tvdbId) {
		this.tvdbId = tvdbId;
	}
	
	//Notkun: episode.setUrl(url)
	//Eftir: url er slod a episode hja trakt client.
	public void setUrl(String url) {
		this.url = url;
	}
	
	//Notkun: episode.setDataTitle(dataTitle)
	//Eftir: dataTitle er trakt audkenni episode.
	public void setDataTitle(String dataTitle){
		this.dataTitle = dataTitle;
	}
	
	
	//Notkun: episode.setShowTitle(showTitle)
	//Eftir: showTitle er titill thattaradarinnar sem episode tilheyrir.
	public void setShowTitle(String showTitle){
		this.showTitle = showTitle;
	}
	
	//Notkun: episode.setEpisodesView(episodesView)
	//Eftir: Buid er ad setja episodesView sem EpisodeView fyrir thattinn episode
	public void setEpisodesView(LinearLayout episodesview) {
		this.episodesView = episodesview;
	}
	
	//Notkun: episode.setInfoButton(infobutton)
	//Eftir: Buid er ad setja infobutton sem takka fyrir upplysingar 
	//				 fyrir thattinn episode
	public void setInfoButton(ImageButton infobutton) {
		this.infoButton = infobutton;
	}
}
