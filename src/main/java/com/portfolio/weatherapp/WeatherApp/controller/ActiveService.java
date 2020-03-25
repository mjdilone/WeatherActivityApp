package com.portfolio.weatherapp.WeatherApp.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class ActiveService {
	private static Logger log = LoggerFactory.getLogger(ActiveService.class);
	
	//client and response pair
	private OkHttpClient client;
	private Response response;
	
	private String data;
	
	//activity arguments
	//type of activity, such as running or swimming
	private String type;
	//category, such as event etc
	private String category;
	//the date the event will start
	private String startDate;
	//the area for which activities will be searched
	private String nearArea;
	//search radius
	private int radius;
	
	public String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	//retrieves a list of activities as a  JSON object
	public JSONObject fetchActivityList(String city) {
		client = new OkHttpClient();
		
		String callUrl = "http://api.amp.active.com/v2/search?query=running&category=event&start_date=" + 
		getCurrentDate() + 
		"..&near=" + 
		city + 
		"&radius=25&api_key=" + 
		Constants.activityAPIKey;
		
		log.info("Activity API URL " + callUrl);
		System.out.println(callUrl);
		
		Request request = new Request.Builder().url(callUrl).build();
		
		try {
			response = client.newCall(request).execute();
			return new JSONObject(response.body().string());
		} catch (Exception e) {
			log.info("Activity API has failed");
		}
		return null;
	}
	
//	public JSONArray returnActivityArray() {
//		//when a page returns JSON it might return an array of JSON objects or even a two dimensional JSON array
//		JSONArray activityJSONArray = fetchActivityList().getJSONArray("results");
//		
//		return activityJSONArray;
//	}
	
	public static Logger getLog() {
		return log;
	}


	public static void setLog(Logger log) {
		ActiveService.log = log;
	}


	public OkHttpClient getClient() {
		return client;
	}


	public void setClient(OkHttpClient client) {
		this.client = client;
	}


	public Response getResponse() {
		return response;
	}


	public void setResponse(Response response) {
		this.response = response;
	}


	public String getData() {
		return data;
	}


	public void setData(String data) {
		this.data = data;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getStartDate() {
		return startDate;
	}


	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	public String getNearArea() {
		return nearArea;
	}


	public void setNearArea(String nearArea) {
		this.nearArea = nearArea;
	}


	public int getRadius() {
		return radius;
	}


	public void setRadius(int radius) {
		this.radius = radius;
	}
}
