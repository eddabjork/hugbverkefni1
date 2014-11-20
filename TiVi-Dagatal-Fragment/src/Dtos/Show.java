/**
 * Nafn:		Steinunn Fri�geirsd�ttir
 * Dagsetning:	2. okt�ber 2014
 * Markmi�:		Klasinn er hlutur sem geymir uppl�singar um ��ttar��.
 **/
package Dtos;

import java.util.List;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

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
	private List<Season> seasons;
	private LinearLayout infoLayout;
	private LinearLayout infoMain;
	private ScrollView scrollView;
	private ImageButton infoButton;
	
	public Show(){}
	
	public Show(String title){
		this.title = title;
	}
	
	
	//Getters
	
	//Notkun: airDay = show.getAirDay()
	//Eftirskilyr�i: airDay frums�ningardegi ��ttara�ar (vikudegi).
	public String getAirDay() {
		return airDay;
	}
	
	//Notkun: airTime = show.getAirTime()
	//Eftirskilyr�i: airTime er frums�ningart�mi ��ttara�ar � USA.
	public String getAirTime() {
		return airTime;
	}
	
	//Notkun: url = show.getBanner()
	//Eftirskilyr�i: url er sl�� � banner mynd ��ttara�ar.
	public String getBanner() {
		return banner;
	}
	
	//Notkun: country = show.getCountry()
	//Eftirskilyr�i: country er upprunalandi ��ttara�ar.
	public String getCountry() {
		return country;
	}
	
	//Notkun: ended = show.isEnded()
	//Eftirskilyr�i: ended er true e�a false eftir �v� 
	//				 hvort ��ttar�� s� h�tt � s�ningu e�a ekki.
	public boolean isEnded() {
		return ended;
	}
	
	//Notkun: fanart = show.getFanart()
	//Eftirskilyr�i: fanart er sl�� � ��d�endalist ��ttara�ar.
	public String getFanart() {
		return fanart;
	}
	
	//Notkun: firstAired = show.getFirstAired()
	//Eftirskilyr�i: Skilar dagsetningunni sem ��tturinn er s�ndur fyrst.
	public String getFirstAired() {
		return firstAired;
	}
	
	//Notkun: genres = show.getGenres()
	//Eftirskilyr�i: genres er tegund ��ttara�ar.
	public List<String> getGenres() {
		return genres;
	}
	
	//Notkun: imdbId = show.getImdbId()
	//Eftirskilyr�i: imdbId er IMDb au�kenni ��ttara�ar.
	public String getImdbId() {
		return imdbId;
	}
	
	//Notkun: network = show.getNetwork()
	//Eftirskilyr�i: network er st�� sem ��ttar�� er s�nd � � USA.
	public String getNetwork() {
		return network;
	}
	
	//Notkun: overview = show.getOverview()
	//Eftirskilyr�i: overview er l�sing � ��ttar��.
	public String getOverview() {
		return overview;
	}
	
	//Notkun: poster = show.getPoster()
	//Eftirskilyr�i: poster er sl�� � fors��umynd ��ttara�ar.
	public String getPoster() {
		return poster;
	}
	
	//Notkun: title = show.getTitle()
	//Eftirskilyr�i: title er titill ��ttara�ar.
	public String getTitle() {
		return title;
	}
	
	//Notkun: tvdbId = show.getTvdbId()
	//Eftirskilyr�i: tvdbId er Tvdb au�kenni.
	public String getTvdbId() {
		return tvdbId;
	}
	
	//Notkun: url = show.getUrl()
	//Eftirskilyr�i: url er sl�� � ��ttar�� � trakt s��unni.
	public String getUrl() {
		return url;
	}
	
	//Notkun: year = show.getYear()
	//Eftirskilyr�i: year er �r sem ��ttar�� kom �t.
	public String getYear() {
		return year;
	}
	//Notkun: dataTitle = show.getDataTitle()
	//Eftirskilyr�i: dataTitle er au�kenni ��ttara�ar fyrir vef�j�nustu.
	public String getDataTitle(){
		return dataTitle;
	}
	
	//Notkun: rating = show.getImdbRating()
	//Eftirskilyr�i: rating er IMDb einkunn ��ttara�ar.
	public String getImdbRating(){
		return imdbRating;
	}
	
	//Notkun: show.getSeasons(seasons)
	//Eftirskilyr�i: seasons eru ser�ur show.
	public List<Season> getSeasons() {
		return seasons;
	}
	
	public LinearLayout getInfoLayout() {
		return this.infoLayout;
	}
	
	public LinearLayout getInfoMain() {
		return this.infoMain;
	}
	
	public ScrollView getScrollView() {
		return this.scrollView;
	}
	
	public ImageButton getInfoButton() {
		return this.infoButton;
	}
	
	/*Setters*/
	
	//Notkun: show.setAirDay(airDay)
	//Eftirskilyr�i: airDay er furms�ningaradagur show.
	public void setAirDay(String airDay) {
		this.airDay = airDay;
	}
	
	//Notkun: show.setAirTime(airTime)
	//Eftirskilyr�i: airTime er frums�ningart�mi show.
	public void setAirTime(String airTime) {
		this.airTime = airTime;
	}
	
	//Notkun: show.setBanner(banner)
	//Eftirskilyr�i: banner er url � banner mynd show.
	public void setBanner(String banner) {
		this.banner = banner;
	}

	//Notkun: show.setCountry(country)
	//Eftirskilyr�i: country er s�ningarland show.
	public void setCountry(String country) {
		this.country = country;
	}

	//Notkun: show.setEnded(ended)
	//Eftirskilyr�i: ended er true e�a false eftir �v� hvort show er � s�ningu e�a ekki.
	public void setEnded(boolean ended) {
		this.ended = ended;
	}

	//Notkun: show.setFanart(fanart)
	//Eftirskilyr�i: fanart er url � a�d�endalist show.
	public void setFanart(String fanart) {
		this.fanart = fanart;
	}

	//Notkun: show.setFirstAired(firstAired)
	//Eftirskilyr�i: firstAired frums�ningardagsetning show.
	public void setFirstAired(String firstAired) {
		this.firstAired = firstAired;
	}

	//Notkun: show.setGenres(genres)
	//Eftirskilyr�i: genres er tegund show.
	public void setGenres(List<String> genres) {
		this.genres = genres;
	}

	//Notkun: show.setImdbId(imdbId)
	//Eftirskilyr�i: imdbId er IMDb aukenni show.
	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}

	//Notkun: show.setNetwork(network)
	//Eftirskilyr�i: network er s� st�� sem show er s�nd �.
	public void setNetwork(String network) {
		this.network = network;
	}

	//Notkun: show.setOverview(overview)
	//Eftirskilyr�i: overview er l�sing � show.
	public void setOverview(String overview) {
		this.overview = overview;
	}

	//Notkun: show.setPoster(poster)
	//Eftirskilyr�i: poster er url � a�almynd show.
	public void setPoster(String poster) {
		this.poster = poster;
	}
	
	//Notkun: show.setTitle(title)
	//Eftirskilyr�i: title er titill show.
	public void setTitle(String title) {
		this.title = title;
	}

	//Notkun: show.setTvdbId(id)
	//Eftirskilyr�i: id er Tvdb au�kenni show.
	public void setTvdbId(String tvdbId) {
		this.tvdbId = tvdbId;
	}

	//Notkun: show.setUrl(url)
	//Eftirskilyr�i: url er sl�� show � trakt.
	public void setUrl(String url) {
		this.url = url;
	}

	//Notkun: show.setYear(Year)
	//Eftirskilyr�i: year er �ri� sem show var gefinn �t fyrst.
	public void setYear(String year) {
		this.year = year;
	}

	//Notkun: show.setDataTitle(title)
	//Eftirskilyr�i: title er aukenni show � trakt.
	public void setDataTitle(String dataTitle){
		this.dataTitle = dataTitle;
	}

	//Notkun: show.setImdbRating(rating)
	//Eftirskilyr�i: rating er IMDb einkunn show.
	public void setImdbRating(String rating){
		this.imdbRating = rating;
	}
	
	//Notkun: show.setSeasons(seasons)
	//Eftirskilyr�i: seasons eru ser�ur show.
	public void setSeasons(List<Season> seasons) {
		this.seasons = seasons;
	}
	
	public void setInfoLayout(LinearLayout infolayout) {
		this.infoLayout = infolayout;
	}
	
	public void setInfoMain(LinearLayout infomain) {
		this.infoMain = infomain;
	}
	
	public void setScrollView(ScrollView scrollview) {
		this.scrollView = scrollview;
	}
	
	public void setInfoButton(ImageButton infobutton) {
		this.infoButton = infobutton;
	}
}
