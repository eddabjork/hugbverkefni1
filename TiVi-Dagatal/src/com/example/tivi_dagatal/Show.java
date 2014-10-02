package com.example.tivi_dagatal;

public class Show {
	String title;
	String year;
	String url;
	String firstAried;
	String country;
	String overview;
	String network;
	String airDay;
	String airTime;
	String imdbId;
	String tvdbId;
	boolean ended;
	String genres[];
	String poster;
	String fanart;
	String banner;
	
	public Show(){}
	
	public Show(String title){
		this.title = title;
	}
	
	//Getters
	public String getTitle(){
		return this.title;
	}
	
	public String getYear(){
		return this.year;
	}
	
	//Setters
	public void setTitle(String Title){
		this.title = Title;
	}
	
	void setYear(String Year){
		this.year = Year;
	}
}
