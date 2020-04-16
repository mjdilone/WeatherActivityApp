package com.portfolio.weatherapp.WeatherApp.controller;

import com.vaadin.server.Page;

public class Constants {
	public static final String activityAPIKey = "uj38sx9s6xzvvksbxyutgqh6";
	
	//dummy values for testing
	public static final String testWeatherCityInput = "Brooklyn";
	//resources
	public static final String locationMarkerFileLocation = "src/main/resources/img/icons/77_Essential_Icons_Location Marker.png";
	//styles
	public static final String slideInUp = "animated slideInUp";
	public static final String slideOutUp = "animated slideOutUp";
	
	public static String getPanelWidth(double browserPageWidth) {
		double panelWidth = browserPageWidth * .92;
		return Double.toString(panelWidth);
	}
	
	public static String getPanelWidth() {
		double browserPageWidth = Page.getCurrent().getBrowserWindowWidth();
		return Double.toString(browserPageWidth * .92);
	}
}