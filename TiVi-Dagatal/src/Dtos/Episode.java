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
	
	/*Getters*/
	
	//Notkun: first = episode.getFirstAired()
	//Eftirskilyrði: first er frumsýningardagur episode.
	public String getFirstAired() {
		return firstAired;
	}
	
	//Notkun: imdbId = episode.getImdbId()
	//Eftirskilyrði: imdbId er IMDb auðkenni episode.
	public String getImdbId() {
		return imdbId;
	}
		
	//Notkun: number = episode.getNumber()
	//Eftirskilyrði: number er þáttanúmer episode.
	public String getNumber() {
		return number;
	}
		
	//Notkun: overview = episode.getOverview()
	//Eftirskilyrði: overview er lýsing á episode.
	public String getOverview() {
		return overview;
	}
		
	//Notkun: ratingPer = episode.getRatingPercentage()
	//Eftirskilyrði: ratingPer er hlutfall einkunna episode.
	public String getRatingPercentage() {
		return ratingPercentage;
	}
		
	//Notkun: screen = episode.getScreen()
	//Eftirskilyrði: screen er mynd úr episode.
	public String getScreen() {
		return screen;
	}
		
	//Notkun: season = episode.getSeason()
	//Eftirskilyrði: season er númer seríu sem episode er í.
	public String getSeason() {
		return season;
	}
		
	//Notkun: title = episode.getTitle()
	//Eftirskilyrði: title er titill episode.
	public String getTitle() {
		return title;
	}
		
	//Notkun: id = episode.getTvdbId()
	//Eftirskilyrði: id er Tvdb auðkenni episode.
	public String getTvdbId() {
		return tvdbId;
	}
		
	//Notkun: ratingPer = episode.getRatingPercentage()
	//Eftirskilyrði: ratingPer er hlutfall einkunna episode.
	public String getUrl() {
		return url;
	}
		
	//Notkun: dataTitle = episode.getDataTitle()
	//Eftirskilyrði: dataTitle er trakt auðkenni episode.
	public String getDataTitle(){
		return dataTitle;
	}
	
	
	/*Setters*/
	
	//Notkun: episode.setFirstAired(firstAired)
	//Eftirskilyrði: firstAired er frumsýningardagur episode.
	public void setFirstAired(String firstAired) {
		this.firstAired = firstAired;
	}
	
	//Notkun: episode.setImdbId(id)
	//Eftirskilyrði: id er IMDb auðkenni episode.
	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}
	
	//Notkun: episode.setNumber(number)
	//Eftirskilyrði: number er númer episode í seríu.
	public void setNumber(String number) {
		this.number = number;
	}
	
	//Notkun: episode.setOverview(overview)
	//Eftirskilyrði: overview er lýsing á episode.
	public void setOverview(String overview) {
		this.overview = overview;
	}
	
	//Notkun: episode.setRatingPercentage(rat)
	//Eftirskilyrði: rat er hlutfall einkunna á episode.
	public void setRatingPercentage(String ratingPercentage) {
		this.ratingPercentage = ratingPercentage;
	}
	
	//Notkun: episode.setScreen(screen)
	//Eftirskilyrði: screen er mynd úr episode.
	public void setScreen(String screen) {
		this.screen = screen;
	}
	
	//Notkun: episode.setSeason(season)
	//Eftirskilyrði: season er númer seríu sem episode er í.
	public void setSeason(String season) {
		this.season = season;
	}
	
	//Notkun: episode.setTitle(title)
	//Eftirskilyrði: title er titill episode.
	public void setTitle(String title) {
		this.title = title;
	}
	
	//Notkun: episode.setTvdbId(id)
	//Eftirskilyrði: id er Tvdb auðkenni episode.
	public void setTvdbId(String tvdbId) {
		this.tvdbId = tvdbId;
	}
	
	//Notkun: episode.setUrl(url)
	//Eftirskilyrði: url er slóð á episode í trakt client.
	public void setUrl(String url) {
		this.url = url;
	}
	
	//Notkun: episode.setDataTitle(dataTitle)
	//Eftirskilyrði: dataTitle er trakt auðkenni episode.
	public void setDataTitle(String dataTitle){
		this.dataTitle = dataTitle;
	}
}
