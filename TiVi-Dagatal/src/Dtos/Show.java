/**
 * Nafn:		Steinunn Friðgeirsdóttir
 * Dagsetning:	2. október 2014
 * Markmið:		Klasinn er hlutur sem geymir upplýsingar um þáttaröð.
 **/
package Dtos;

import java.util.List;

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
	private List<String> genres;
	private String poster;
	private String fanart;
	private String banner;
	private String imdbRating;
	
	public Show(){}
	
	public Show(String title){
		this.title = title;
	}
	
	
	//Getters
	
	//Notkun: airDay = show.getAirDay()
	//Eftirskilyrði: airDay frumsýningardegi þáttaraðar (vikudegi).
	public String getAirDay() {
		return airDay;
	}
	
	//Notkun: airTime = show.getAirTime()
	//Eftirskilyrði: airTime er frumsýningartími þáttaraðar í USA.
	public String getAirTime() {
		return airTime;
	}
	
	//Notkun: url = show.getBanner()
	//Eftirskilyrði: url er slóð á banner mynd þáttaraðar.
	public String getBanner() {
		return banner;
	}
	
	//Notkun: country = show.getCountry()
	//Eftirskilyrði: country er upprunalandi þáttaraðar.
	public String getCountry() {
		return country;
	}
	
	//Notkun: ended = show.isEnded()
	//Eftirskilyrði: ended er true eða false eftir því 
	//				 hvort þáttaröð sé hætt í sýningu eða ekki.
	public boolean isEnded() {
		return ended;
	}
	
	//Notkun: fanart = show.getFanart()
	//Eftirskilyrði: fanart er slóð á áðdáendalist þáttaraðar.
	public String getFanart() {
		return fanart;
	}
	
	//Notkun: firstAired = show.getFirstAired()
	//Eftirskilyrði: Skilar dagsetningunni sem þátturinn er sýndur fyrst.
	public String getFirstAired() {
		return firstAired;
	}
	
	//Notkun: genres = show.getGenres()
	//Eftirskilyrði: genres er tegund þáttaraðar.
	public List<String> getGenres() {
		return genres;
	}
	
	//Notkun: imdbId = show.getImdbId()
	//Eftirskilyrði: imdbId er IMDb auðkenni þáttaraðar.
	public String getImdbId() {
		return imdbId;
	}
	
	//Notkun: network = show.getNetwork()
	//Eftirskilyrði: network er stöð sem þáttaröð er sýnd á í USA.
	public String getNetwork() {
		return network;
	}
	
	//Notkun: overview = show.getOverview()
	//Eftirskilyrði: overview er lýsing á þáttaröð.
	public String getOverview() {
		return overview;
	}
	
	//Notkun: poster = show.getPoster()
	//Eftirskilyrði: poster er slóð á forsíðumynd þáttaraðar.
	public String getPoster() {
		return poster;
	}
	
	//Notkun: title = show.getTitle()
	//Eftirskilyrði: title er titill þáttaraðar.
	public String getTitle() {
		return title;
	}
	
	//Notkun: tvdbId = show.getTvdbId()
	//Eftirskilyrði: tvdbId er Tvdb auðkenni.
	public String getTvdbId() {
		return tvdbId;
	}
	
	//Notkun: url = show.getUrl()
	//Eftirskilyrði: url er slóð á þáttaröð á trakt síðunni.
	public String getUrl() {
		return url;
	}
	
	//Notkun: year = show.getYear()
	//Eftirskilyrði: year er ár sem þáttaröð kom út.
	public String getYear() {
		return year;
	}
	//Notkun: dataTitle = show.getDataTitle()
	//Eftirskilyrði: dataTitle er auðkenni þáttaraðar fyrir vefþjónustu.
	public String getDataTitle(){
		return dataTitle;
	}
	
	//Notkun: rating = show.getImdbRating()
	//Eftirskilyrði: rating er IMDb einkunn þáttaraðar.
	public String getImdbRating(){
		return imdbRating;
	}
	
	/*Setters*/
	
	//Notkun: show.setAirDay(airDay)
	//Eftirskilyrði: airDay er furmsýningaradagur show.
	public void setAirDay(String airDay) {
		this.airDay = airDay;
	}
	
	//Notkun: show.setAirTime(airTime)
	//Eftirskilyrði: airTime er frumsýningartími show.
	public void setAirTime(String airTime) {
		this.airTime = airTime;
	}
	
	//Notkun: show.setBanner(banner)
	//Eftirskilyrði: banner er url á banner mynd show.
	public void setBanner(String banner) {
		this.banner = banner;
	}

	//Notkun: show.setCountry(country)
	//Eftirskilyrði: country er sýningarland show.
	public void setCountry(String country) {
		this.country = country;
	}

	//Notkun: show.setEnded(ended)
	//Eftirskilyrði: ended er true eða false eftir því hvort show er í sýningu eða ekki.
	public void setEnded(boolean ended) {
		this.ended = ended;
	}

	//Notkun: show.setFanart(fanart)
	//Eftirskilyrði: fanart er url á aðdáendalist show.
	public void setFanart(String fanart) {
		this.fanart = fanart;
	}

	//Notkun: show.setFirstAired(firstAired)
	//Eftirskilyrði: firstAired frumsýningardagsetning show.
	public void setFirstAired(String firstAired) {
		this.firstAired = firstAired;
	}

	//Notkun: show.setGenres(genres)
	//Eftirskilyrði: genres er tegund show.
	public void setGenres(List<String> genres) {
		this.genres = genres;
	}

	//Notkun: show.setImdbId(imdbId)
	//Eftirskilyrði: imdbId er IMDb aukenni show.
	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}

	//Notkun: show.setNetwork(network)
	//Eftirskilyrði: network er sú stöð sem show er sýnd á.
	public void setNetwork(String network) {
		this.network = network;
	}

	//Notkun: show.setOverview(overview)
	//Eftirskilyrði: overview er lýsing á show.
	public void setOverview(String overview) {
		this.overview = overview;
	}

	//Notkun: show.setPoster(poster)
	//Eftirskilyrði: poster er url á aðalmynd show.
	public void setPoster(String poster) {
		this.poster = poster;
	}
	
	//Notkun: show.setTitle(title)
	//Eftirskilyrði: title er titill show.
	public void setTitle(String title) {
		this.title = title;
	}

	//Notkun: show.setTvdbId(id)
	//Eftirskilyrði: id er Tvdb auðkenni show.
	public void setTvdbId(String tvdbId) {
		this.tvdbId = tvdbId;
	}

	//Notkun: show.setUrl(url)
	//Eftirskilyrði: url er slóð show á trakt.
	public void setUrl(String url) {
		this.url = url;
	}

	//Notkun: show.setYear(Year)
	//Eftirskilyrði: year er árið sem show var gefinn út fyrst.
	public void setYear(String year) {
		this.year = year;
	}

	//Notkun: show.setDataTitle(title)
	//Eftirskilyrði: title er aukenni show á trakt.
	public void setDataTitle(String dataTitle){
		this.dataTitle = dataTitle;
	}

	//Notkun: show.setImdbRating(rating)
	//Eftirskilyrði: rating er IMDb einkunn show.
	public void setImdbRating(String rating){
		this.imdbRating = rating;
	}
}
