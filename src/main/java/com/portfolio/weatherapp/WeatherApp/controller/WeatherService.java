package com.portfolio.weatherapp.WeatherApp.controller;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class WeatherService {

	//create an http client and response
	private OkHttpClient client;
	private Response response;
	
	private String cityName;
	private String unit;
	
	//the payload from using the weather api is a JSON object
	public JSONObject getWeather() {
		client = new OkHttpClient();
		//https://samples.openweathermap.org/data/2.5/find?q=London&units=imperial&appid=b6907d289e10d714a6e88b30761fae22
		String callUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + getCityName() + "&units=" + getUnit()  + "&appid=5775ef39fc4241a37080472d52bb9590";
		System.out.println("callUrl: " + callUrl);
		//builds a request from the address of the api your using
		Request request = new Request.Builder().url(callUrl).build();
		
		try {
			response = client.newCall(request).execute();
			return new JSONObject(response.body().string());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public JSONArray returnWeatherArray() {
		//when a page returns JSON it might return an array of JSON objects or even a two dimensional JSON array
		JSONArray weatherJsonArray = getWeather().getJSONArray("weather");
		
		return weatherJsonArray;
	}
	
	public JSONObject returnMain() {
		//here we are first getting the complete JSON object(which itself is an array of objects) from the weather api
		//then we retrieve the element of that bigger array that has the name main
		JSONObject mainObject = getWeather().getJSONObject("main");
		
		return mainObject;
	}
	
	public JSONObject returnWind() {
		JSONObject windObject = getWeather().getJSONObject("wind");
		
		return windObject;
	}
	
	public JSONObject returnSunSet() {
		JSONObject sunsetObject = getWeather().getJSONObject("sys");
		
		return sunsetObject;
	}
	
	
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}