package com.portfolio.weatherapp.WeatherApp.model;

public class Activity {
	private String description = null;
	private String url = null;
	private String location = null;
	private String startDate = null;
	private String logoImageURL = null;
	
	
	public Activity(String description, String url, String location, String startDate, String logoImageURL) {
		super();
		this.description = description;
		this.url = url;
		this.location = location;
		this.startDate = startDate;
		this.logoImageURL = logoImageURL;
	}
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getURL() {
		return url;
	}
	public void setURL(String url) {
		this.url = url;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getLogoImageURL() {
		return logoImageURL;
	}
	public void setLogoImageURL(String logoImageURL) {
		this.logoImageURL = logoImageURL;
	}
}