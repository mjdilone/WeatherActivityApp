package com.portfolio.weatherapp.WeatherApp.model;

public class Activity {
	private String description;
	private String url;
	private String location;
	private String startDate;
	private String logoImageURL;
	private String name;
	

	
	public Activity() {
		super();
	}

	public Activity(String description, String url, String location, String startDate, String logoImageURL,String name) {
		super();
		this.description = description;
		this.url = url;
		this.location = location;
		this.startDate = startDate;
		this.logoImageURL = logoImageURL;
		this.name = name;
	}
	
	
//	@Override
//	public String toString() {
//		return "Activity [description=" + description + "\n" +  ", url=" + url + ", location=" + location + ", startDate="
//				+ startDate + ", logoImageURL=" + logoImageURL + ", name=" + name + "]";
//	}
	
	@Override
	public String toString() {
		return ", name=" + name + "]";
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
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