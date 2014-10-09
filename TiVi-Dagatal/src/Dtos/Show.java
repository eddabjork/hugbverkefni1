package Dtos;

public class Show {
	private String title;
	private String dataTitle;
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
	private String genres[];
	private String poster;
	private String fanart;
	private String banner;
	private String imdbRating;
	
	public Show(){}
	
	public Show(String title){
		this.title = title;
	}
	
	//Getters
	public String getAirDay() {
		return airDay;
	}
	
	public String getAirTime() {
		return airTime;
	}
	
	public String getBanner() {
		return banner;
	}
	
	public String getCountry() {
		return country;
	}
	
	public boolean isEnded() {
		return ended;
	}
	
	public String getFanart() {
		return fanart;
	}
	
	public String getFirstAired() {
		return firstAired;
	}
	
	public String[] getGenres() {
		return genres;
	}
	
	public String getImdbId() {
		return imdbId;
	}
	
	public String getNetwork() {
		return network;
	}
	
	public String getOverview() {
		return overview;
	}
	
	public String getPoster() {
		return poster;
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
	
	public String getYear() {
		return year;
	}
	
	public String getDataTitle(){
		return dataTitle;
	}
	
	public String getImdbRating(){
		return imdbRating;
	}
	
	//Setters
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
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setTvdbId(String tvdbId) {
		this.tvdbId = tvdbId;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setYear(String year) {
		this.year = year;
	}
	
	public void setDataTitle(String dataTitle){
		this.dataTitle = dataTitle;
	}
	
	public void setImdbRating(String rating){
		this.imdbRating = rating;
	}
}
