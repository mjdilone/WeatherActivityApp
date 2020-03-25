package com.portfolio.weatherapp.WeatherApp.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	public static String convertTime(long time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yy hh.mm aa");
		
		return dateFormat.format(new Date(time));
	}
}
