/**
 * Nafn:		Steinunn Fridgeirsdottir
 * Dagsetning:	2. oktober 2014
 * Markmid:		Klasinn er hlutur sem geymir upplysingar um thattarod.
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
	//Eftir: airDay frumsyningardegi thattaradarinnar (vikudegi).
	public String getAirDay() {
		return airDay;
	}
	
	//Notkun: airTime = show.getAirTime()
	//Eftir: airTime er frumsyningartimi thattaradarinnar i USA.
	public String getAirTime() {
		return airTime;
	}
	
	//Notkun: url = show.getBanner()
	//Eftir: url er slod a banner mynd thattaradarinnar.
	public String getBanner() {
		return banner;
	}
	
	//Notkun: country = show.getCountry()
	//Eftir: country er upprunalandi thattaradarinnar.
	public String getCountry() {
		return country;
	}
	
	//Notkun: ended = show.isEnded()
	//Eftir: ended er true eda false eftir thvi 
	//				 hvort thattarodin se haett i syningu eda ekki.
	public boolean isEnded() {
		return ended;
	}
	
	//Notkun: fanart = show.getFanart()
	//Eftir: fanart er slod a addaendalist thattaradarinnar.
	public String getFanart() {
		return fanart;
	}
	
	//Notkun: firstAired = show.getFirstAired()
	//Eftir: Skilar dagsetningunni sem thatturinn er syndur fyrst.
	public String getFirstAired() {
		return firstAired;
	}
	
	//Notkun: genres = show.getGenres()
	//Eftir: genres eru tegundir thattaradarinnar.
	public List<String> getGenres() {
		return genres;
	}
	
	//Notkun: imdbId = show.getImdbId()
	//Eftir: imdbId er IMDb audkenni thattaradarinnar.
	public String getImdbId() {
		return imdbId;
	}
	
	//Notkun: network = show.getNetwork()
	//Eftir: network er sjonvarpsstodin sem thattarodin er synd a i USA.
	public String getNetwork() {
		return network;
	}
	
	//Notkun: overview = show.getOverview()
	//Eftir: overview er lysing a thattarodinni.
	public String getOverview() {
		return overview;
	}
	
	//Notkun: poster = show.getPoster()
	//Eftir: poster er slod a forsidumynd thattaradarinnar.
	public String getPoster() {
		return poster;
	}
	
	//Notkun: title = show.getTitle()
	//Eftir: title er titill thattaradarinnar.
	public String getTitle() {
		return title;
	}
	
	//Notkun: tvdbId = show.getTvdbId()
	//Eftir: tvdbId er Tvdb audkenni.
	public String getTvdbId() {
		return tvdbId;
	}
	
	//Notkun: url = show.getUrl()
	//Eftir: url er slodin a thattarodina hja trakt sidunni.
	public String getUrl() {
		return url;
	}
	
	//Notkun: year = show.getYear()
	//Eftir: year er arid sem thattarodin kom ut.
	public String getYear() {
		return year;
	}
	//Notkun: dataTitle = show.getDataTitle()
	//Eftir: dataTitle er audkenni thattaradarinnar fyrir vefthjonustu.
	public String getDataTitle(){
		return dataTitle;
	}
	
	//Notkun: rating = show.getImdbRating()
	//Eftir: rating er IMDb einkunn thattaradarinnar.
	public String getImdbRating(){
		return imdbRating;
	}
	
	//Notkun: show.getSeasons(seasons)
	//Eftir: seasons eru seriur show.
	public List<Season> getSeasons() {
		return seasons;
	}
	
	//Notkun: infoLayout = show.getInfoLayout()
	//Eftir: infoLayout er layout-id fyrir upplysingar um thattaradir
	//				 fyrir thattarodina show
	public LinearLayout getInfoLayout() {
		return this.infoLayout;
	}
	
	//Notkun: infoMain = show.getInfoMain()
	//Eftir: infoMain er adal-layout-id fyrir upplysingar um thattaradir
	//				 fyrir thattarodina show
	public LinearLayout getInfoMain() {
		return this.infoMain;
	}
	
	//Notkun: scrollView = show.getScrollView()
	//Eftir: scrollView er ScrollView-id fyrir thattarodina show
	public ScrollView getScrollView() {
		return this.scrollView;
	}
	
	//Notkun: infoButton = show.getInfoButton()
	//Eftir: infoButton er takkinn fyrir upplysingar um thattaradir
	//				 fyrir thattarodina show
	public ImageButton getInfoButton() {
		return this.infoButton;
	}
	
	/*Setters*/
	
	//Notkun: show.setAirDay(airDay)
	//Eftir: airDay er furmsyningaradagur show.
	public void setAirDay(String airDay) {
		this.airDay = airDay;
	}
	
	//Notkun: show.setAirTime(airTime)
	//Eftir: airTime er frumsyningartimi show.
	public void setAirTime(String airTime) {
		this.airTime = airTime;
	}
	
	//Notkun: show.setBanner(banner)
	//Eftir: banner er url a banner mynd show.
	public void setBanner(String banner) {
		this.banner = banner;
	}

	//Notkun: show.setCountry(country)
	//Eftir: country er syningarland show.
	public void setCountry(String country) {
		this.country = country;
	}

	//Notkun: show.setEnded(ended)
	//Eftir: ended er true eda false eftir thvi hvort show er i syningu eda ekki.
	public void setEnded(boolean ended) {
		this.ended = ended;
	}

	//Notkun: show.setFanart(fanart)
	//Eftir: fanart er url a addaendalist show.
	public void setFanart(String fanart) {
		this.fanart = fanart;
	}

	//Notkun: show.setFirstAired(firstAired)
	//Eftir: firstAired frumsyningardagsetning show.
	public void setFirstAired(String firstAired) {
		this.firstAired = firstAired;
	}

	//Notkun: show.setGenres(genres)
	//Eftir: genres er tegund show.
	public void setGenres(List<String> genres) {
		this.genres = genres;
	}

	//Notkun: show.setImdbId(imdbId)
	//Eftir: imdbId er IMDb aukenni show.
	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}

	//Notkun: show.setNetwork(network)
	//Eftir: network er su sjonvarpsstod sem show er synd a.
	public void setNetwork(String network) {
		this.network = network;
	}

	//Notkun: show.setOverview(overview)
	//Eftir: overview er lysing a show.
	public void setOverview(String overview) {
		this.overview = overview;
	}

	//Notkun: show.setPoster(poster)
	//Eftir: poster er url a adalmynd show.
	public void setPoster(String poster) {
		this.poster = poster;
	}
	
	//Notkun: show.setTitle(title)
	//Eftir: title er titill show.
	public void setTitle(String title) {
		this.title = title;
	}

	//Notkun: show.setTvdbId(id)
	//Eftir: id er Tvdb audkenni show.
	public void setTvdbId(String tvdbId) {
		this.tvdbId = tvdbId;
	}

	//Notkun: show.setUrl(url)
	//Eftir: url er slod a show hja trakt.
	public void setUrl(String url) {
		this.url = url;
	}

	//Notkun: show.setYear(Year)
	//Eftir: year er arid sem show var gefinn ut fyrst.
	public void setYear(String year) {
		this.year = year;
	}

	//Notkun: show.setDataTitle(title)
	//Eftir: title er aukenni show hja trakt.
	public void setDataTitle(String dataTitle){
		this.dataTitle = dataTitle;
	}

	//Notkun: show.setImdbRating(rating)
	//Eftir: rating er IMDb einkunn show.
	public void setImdbRating(String rating){
		this.imdbRating = rating;
	}
	
	//Notkun: show.setSeasons(seasons)
	//Eftir: seasons eru seriur show.
	public void setSeasons(List<Season> seasons) {
		this.seasons = seasons;
	}
	
	//Notkun: show.setInfoLayout(infolayout)
	//Eftir: Buid er ad setja infolayout sem layout upplysingar um
	//				 thattaradir fyrir thattarodina show
	public void setInfoLayout(LinearLayout infolayout) {
		this.infoLayout = infolayout;
	}
	
	//Notkun: show.setInfoMain(infomain)
	//Eftir: Buid er ad setja infomain sem adal-layout fyrir upplysingar
	//				 um thattaradir fyrir thattinn show
	public void setInfoMain(LinearLayout infomain) {
		this.infoMain = infomain;
	}
	
	//Notkun: show.setScrollView(scrollview)
	//Eftir: Buid er ad setja scrollview sem ScrollView fyrir thattarodina
	//				 show
	public void setScrollView(ScrollView scrollview) {
		this.scrollView = scrollview;
	}
	
	//Notkun: show.setInfoButton(infobutton)
	//Eftir: Buid er ad setja infobutton sem takka fyrir upplysingar
	//				 fyrir thattarodina show
	public void setInfoButton(ImageButton infobutton) {
		this.infoButton = infobutton;
	}
}
