/**
 * Nafn:		Steinunn Friðgeirsdóttir
 * Dagsetning:	2. október 2014
 * Markmið:		Klasinn er hlutur sem geymir upplýsingar um stakan þátt.
 **/
package Dtos;

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
	
	public Episode(){}
	
	public Episode(String title){
		this.title = title;
	}
	
	//Getters
	public String getFirstAired() {
		return firstAired;
	}
	
	public String getImdbId() {
		return imdbId;
	}
	
	public String getNumber() {
		return number;
	}
	
	public String getOverview() {
		return overview;
	}
	
	public String getRatingPercentage() {
		return ratingPercentage;
	}
	
	public String getScreen() {
		return screen;
	}
	
	public String getSeason() {
		return season;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getTvdbId() {
		return tvdbId;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getDataTitle(){
		return dataTitle;
	}
	
	
	//Setters
	public void setFirstAired(String firstAired) {
		this.firstAired = firstAired;
	}
	
	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public void setOverview(String overview) {
		this.overview = overview;
	}
	
	public void setRatingPercentage(String ratingPercentage) {
		this.ratingPercentage = ratingPercentage;
	}
	
	public void setScreen(String screen) {
		this.screen = screen;
	}
	
	public void setSeason(String season) {
		this.season = season;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setTvdbId(String tvdbId) {
		this.tvdbId = tvdbId;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setDataTitle(String dataTitle){
		this.dataTitle = dataTitle;
	}
}
