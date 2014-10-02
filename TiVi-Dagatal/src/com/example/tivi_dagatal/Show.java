package com.example.tivi_dagatal;

public class Show {
	private String title;
	private String year;
	private String url;
	private String firstAired;
	private String country;
	private String overview;
	private String network;
	private String airDay;
	private String airTime;
	private String imdbId;
	private String tvdbId;
	private boolean ended;
	private String[] genres;
	private String poster;
	private String fanart;
	private String banner;
	
	//Getters
	public String getTitle(){
		return this.title;
	}
	
	public String getYear(){
		return this.year;
	}
	
	public String getUrl(){
		return this.url;
	}
	
	public String getFirstAired(){
		return this.firstAired;
	}
	
	public String getCountry(){
		return this.country;
	}
	
	public String getOverview(){
		return this.overview;
	}
	
	public String getNetwork(){
		return this.network;
	}
	
	public String getAirDay(){
		return this.airDay;
	}
	
	public String getAirTime(){
		return this.airTime;
	}
	
	public String getImdbId(){
		return this.imdbId;
	}
	
	public String getTvdbId(){
		return this.tvdbId;
	}
	
	public boolean getEnded(){
		return this.ended;
	}
	
	public String[] getGenres(){
		return this.genres;
	}
	
	public String getPoster(){
		return this.poster;
	}
	
	public String getFanart(){
		return this.fanart;
	}
	
	public String getBanner(){
		return this.banner;
	}
	
	
	//Setters
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setYear(String year){
		this.year = year;
	}
	
	public void setAirDay(String airDay) {
		this.airDay = airDay;
	}
	
	public void setAirTime(String airTime) {
		this.airTime = airTime;
	}
	
	public void setBanner(String banner) {
		this.banner = banner;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public void setEnded(boolean ended) {
		this.ended = ended;
	}
	
	public void setFanart(String fanart) {
		this.fanart = fanart;
	}
	
	public void setFirstAired(String firstAired) {
		this.firstAired = firstAired;
	}
	
	public void setGenres(String[] genres) {
		this.genres = genres;
	}
	
	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}
	
	public void setNetwork(String network) {
		this.network = network;
	}
	
	public void setOverview(String overview) {
		this.overview = overview;
	}
	
	public void setPoster(String poster) {
		this.poster = poster;
	}
	
	public void setTvdbId(String tvdbId) {
		this.tvdbId = tvdbId;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
}
