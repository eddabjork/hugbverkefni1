/**
 * Nafn:		Steinunn Fri�geirsd�ttir
 * Dagsetning:	2. okt�ber 2014
 * Markmi�:		Klasinn er hlutur sem geymir uppl�singar um stakan ��tt.
 **/
package Dtos;

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
	
	public Episode(){}
	
	public Episode(String title){
		this.title = title;
	}
	
	/*Getters*/
	
	//Notkun: first = episode.getFirstAired()
	//Eftirskilyr�i: first er frums�ningardagur episode.
	public String getFirstAired() {
		return firstAired;
	}
	
	//Notkun: imdbId = episode.getImdbId()
	//Eftirskilyr�i: imdbId er IMDb au�kenni episode.
	public String getImdbId() {
		return imdbId;
	}
		
	//Notkun: number = episode.getNumber()
	//Eftirskilyr�i: number er ��ttan�mer episode.
	public String getNumber() {
		return number;
	}
		
	//Notkun: overview = episode.getOverview()
	//Eftirskilyr�i: overview er l�sing � episode.
	public String getOverview() {
		return overview;
	}
		
	//Notkun: ratingPer = episode.getRatingPercentage()
	//Eftirskilyr�i: ratingPer er hlutfall einkunna episode.
	public String getRatingPercentage() {
		return ratingPercentage;
	}
		
	//Notkun: screen = episode.getScreen()
	//Eftirskilyr�i: screen er mynd �r episode.
	public String getScreen() {
		return screen;
	}
		
	//Notkun: season = episode.getSeason()
	//Eftirskilyr�i: season er n�mer ser�u sem episode er �.
	public String getSeason() {
		return season;
	}
		
	//Notkun: title = episode.getTitle()
	//Eftirskilyr�i: title er titill episode.
	public String getTitle() {
		return title;
	}
		
	//Notkun: id = episode.getTvdbId()
	//Eftirskilyr�i: id er Tvdb au�kenni episode.
	public String getTvdbId() {
		return tvdbId;
	}
		
	//Notkun: ratingPer = episode.getRatingPercentage()
	//Eftirskilyr�i: ratingPer er hlutfall einkunna episode.
	public String getUrl() {
		return url;
	}
		
	//Notkun: dataTitle = episode.getDataTitle()
	//Eftirskilyr�i: dataTitle er trakt au�kenni episode.
	public String getDataTitle(){
		return dataTitle;
	}
	
	//Notkun: showTitle = episode.getShowTitle()
	//Eftirskilyr�i: showTitle er titill ��ttara�arinnar sem episode tilheyrir.
	public String getShowTitle(){
		return showTitle;
	}
	
	
	/*Setters*/
	
	//Notkun: episode.setFirstAired(firstAired)
	//Eftirskilyr�i: firstAired er frums�ningardagur episode.
	public void setFirstAired(String firstAired) {
		this.firstAired = firstAired;
	}
	
	//Notkun: episode.setImdbId(id)
	//Eftirskilyr�i: id er IMDb au�kenni episode.
	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}
	
	//Notkun: episode.setNumber(number)
	//Eftirskilyr�i: number er n�mer episode � ser�u.
	public void setNumber(String number) {
		this.number = number;
	}
	
	//Notkun: episode.setOverview(overview)
	//Eftirskilyr�i: overview er l�sing � episode.
	public void setOverview(String overview) {
		this.overview = overview;
	}
	
	//Notkun: episode.setRatingPercentage(rat)
	//Eftirskilyr�i: rat er hlutfall einkunna � episode.
	public void setRatingPercentage(String ratingPercentage) {
		this.ratingPercentage = ratingPercentage;
	}
	
	//Notkun: episode.setScreen(screen)
	//Eftirskilyr�i: screen er mynd �r episode.
	public void setScreen(String screen) {
		this.screen = screen;
	}
	
	//Notkun: episode.setSeason(season)
	//Eftirskilyr�i: season er n�mer ser�u sem episode er �.
	public void setSeason(String season) {
		this.season = season;
	}
	
	//Notkun: episode.setTitle(title)
	//Eftirskilyr�i: title er titill episode.
	public void setTitle(String title) {
		this.title = title;
	}
	
	//Notkun: episode.setTvdbId(id)
	//Eftirskilyr�i: id er Tvdb au�kenni episode.
	public void setTvdbId(String tvdbId) {
		this.tvdbId = tvdbId;
	}
	
	//Notkun: episode.setUrl(url)
	//Eftirskilyr�i: url er sl�� � episode � trakt client.
	public void setUrl(String url) {
		this.url = url;
	}
	
	//Notkun: episode.setDataTitle(dataTitle)
	//Eftirskilyr�i: dataTitle er trakt au�kenni episode.
	public void setDataTitle(String dataTitle){
		this.dataTitle = dataTitle;
	}
	
	
	//Notkun: episode.setShowTitle(showTitle)
	//Eftirskilyr�i: showTitle er titill ��ttara�arinnar sem episode tilheyrir.
	public void setShowTitle(String showTitle){
		this.showTitle = showTitle;
	}
	
	public void setEpisodesView(LinearLayout episodesview) {
		this.episodesView = episodesview;
	}
	
	public LinearLayout getEpisodesView() {
		return this.episodesView;
	}
}
