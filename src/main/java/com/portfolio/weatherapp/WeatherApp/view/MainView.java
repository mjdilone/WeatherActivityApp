package com.portfolio.weatherapp.WeatherApp.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.portfolio.weatherapp.WeatherApp.controller.WeatherService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI(path = "")
public class MainView extends UI{
	
	@Autowired
	private WeatherService weatherService;
	
	private boolean enabled = true;
	
	private Image iconImage;
	
	private NativeSelect<String> unitSelect;
	private TextField cityTextField;
	private Button showWeatherButton;
	
	private Label currentLocationTitle;
	private Label currentTemp;
	private Label weatherDescription;
	private Label weatherMin;
	private Label weatherMax;
	private Label pressureLabel;
	private Label humidityLabel;
	private Label windSpeedLabel;
	private Label sunriseLabel;
	private Label sunsetLabel;
	
	private VerticalLayout mainLayout;
	private HorizontalLayout dashBoardMain;
	private VerticalLayout descriptionLayout;
	private HorizontalLayout mainDescriptionLayout;
	private VerticalLayout pressureLayout;
	private HorizontalLayout logoLayout;
	
	
	@Override
	protected void init(VaadinRequest request) {
		
		/************************** Working with JSON part 1**********************/
//		Label label = new Label("Output");
//		
//		try {
//			//this way you can just retrieve data from your JSON object based on mapped values
//			System.out.println("Data: " + weatherService.getWeather("mumbai").get("coord").toString()); 
//			
//			try {
//				JSONArray jsonArray = weatherService.returnWeatherArray("mumbai");
//				for(int i = 0;i<jsonArray.length();i++) {
//					JSONObject weatherObject = jsonArray.getJSONObject(i);
//					System.out.println(
//							"id : " + weatherObject.getInt("id") + 
//							", main: " + weatherObject.getString("main") + 
//							", description: " + weatherObject.getString("description")
//					);
//					
//				}
//			} catch (Exception e) {
//			}
//			
//			//this is an example of returning data that belongs to a JSONObject
//			//
//			JSONObject mainObject = weatherService.returnMain("mumbai");
//			System.out.println("pressure: " + mainObject.getLong("pressure"));
//			System.out.println("temperature: " + mainObject.getInt("temp"));
//			System.out.println("humidity " + mainObject.getInt("humidity"));
//			
//		} catch (JSONException e) {
//			System.out.println("JSON isn't working");
//		}
//		setContent(label);
		
		
		/***************************** Working with JSON part 2*******************/
		
		setUpLayout();
		setHeader();
		setLogo();
		setUpForm();
		dashBoardTitle();
		dashBoardDescription();
		
		//display a notification if the  city search bar is empty
		showWeatherButton.addClickListener(event -> {
			if(!cityTextField.getValue().equals("")) {
				updateUI();

			}else {
				Notification.show("Please enter a city");
			}
		});
		
	}
	
	//sets up all widgets and componenets that will be used. This avoids an issue where some elements will be null before they can be added since they're instantied in other code blocks
	public void setUpLayout() {
		
		iconImage = new Image();
		
		mainDescriptionLayout = new HorizontalLayout();
		descriptionLayout = new VerticalLayout();
		weatherDescription = new Label("Description : Clear Skies");
		weatherMin = new Label("Min : 56F");
		weatherMax = new Label("Max: 89F");
		pressureLayout = new VerticalLayout();
		pressureLabel = new Label("Pressure: 123pa");
		humidityLabel = new Label("Humidity: 34");
		windSpeedLabel = new Label("Wind Speed: 123/hr");
		sunriseLabel = new Label("Sunrise: ");
		sunsetLabel = new Label("Sunset");

		mainLayout = new VerticalLayout();
		mainLayout.setWidth("100%");//allows the layout to work with the entire screen
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		
		mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		setContent(mainLayout);
	}
	
	public void setHeader() {
		HorizontalLayout headerLayout = new HorizontalLayout();
		
		headerLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		Label title = new Label("Weather!");
		
		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_BOLD);
		title.addStyleName(ValoTheme.LABEL_COLORED);
		
		headerLayout.addComponents(title);
		mainLayout.addComponents(headerLayout);
	}
	
	private void setLogo() {
		logoLayout = new HorizontalLayout();
		
		logoLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		//Image icon = new Image(null,new ClassResource("/weather_icon.png"));
		
		//icon.setWidth("125px");
		//icon.setHeight("125px");
		
		//logoLayout.addComponents(icon);
		mainLayout.addComponent(logoLayout);
	}

	private void setUpForm() {
		HorizontalLayout formLayout = new HorizontalLayout();
		
		formLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		formLayout.setSpacing(enabled);
		formLayout.setMargin(enabled);
		
		//create the selection component
		unitSelect = new NativeSelect<>();
		unitSelect.setWidth("40px");
		ArrayList<String> items = new ArrayList<>();
		
		items.add("F");
		items.add("C");
		
		unitSelect.setItems(items);
		unitSelect.setValue(items.get(0));
		formLayout.addComponents(unitSelect);
		
		//add textField
		cityTextField = new TextField();
		cityTextField.setWidth("40%");
		formLayout.addComponents(cityTextField);
		
		//add Button
		showWeatherButton = new Button();
		showWeatherButton.setIcon(VaadinIcons.SEARCH);
		formLayout.addComponents(showWeatherButton);
		
		
		mainLayout.addComponents(formLayout);
	}
	
	private void dashBoardTitle() {
		
		dashBoardMain = new HorizontalLayout();
		
		dashBoardMain.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		currentLocationTitle = new Label("Currently in Spokane");
		currentLocationTitle.addStyleName(ValoTheme.LABEL_H2);
		currentLocationTitle.addStyleName(ValoTheme.LABEL_LIGHT);
		
		//current temp label
		currentTemp = new Label("19F");
		currentTemp.addStyleName(ValoTheme.LABEL_BOLD);
		currentTemp.addStyleName(ValoTheme.LABEL_H1);
		
	}
	
	private void dashBoardDescription() {
		mainDescriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		//Description Veritcal Layout
		descriptionLayout.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		descriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		descriptionLayout.addComponents(weatherDescription);
		descriptionLayout.addComponents(weatherMin);
		descriptionLayout.addComponents(weatherMax);
		
		//Pressure humidity etc
		pressureLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		pressureLayout.addComponent(pressureLabel);
		pressureLayout.addComponent(humidityLabel);
		pressureLayout.addComponent(windSpeedLabel);
		pressureLayout.addComponent(sunriseLabel);
		pressureLayout.addComponent(sunsetLabel);
	}

	private void updateUI() {
		//sets the location title to what's been searched in the city search bar
		currentLocationTitle.setValue("Currently in " + cityTextField.getValue());
		
		JSONObject mainObject = new JSONObject();
		String city = cityTextField.getValue();
		String defaultUnit;
		String unit;
		
		if(unitSelect.getValue().equals("F")) {
			defaultUnit = "imperial";
			unitSelect.setValue("F");
			unit = "\u00b0" + "F";
		}else {
			defaultUnit = "metric";
			unitSelect.setValue("C");
			unit = "\u00b0" + "C";
		}
		
		weatherService.setCityName(city);
		weatherService.setUnit(defaultUnit);
		
		try {
			mainObject = weatherService.returnMain();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int temp = mainObject.getInt("temp");
		currentTemp.setValue(temp + "F");
		
		//get min,max,pressure,humidity
		double minTemp = mainObject.getDouble("temp_min");
		double maxTemp = mainObject.getDouble("temp_max");
		int pressure = mainObject.getInt("pressure");
		int humidity = mainObject.getInt("humidity");
		
		//get wind speed
		JSONObject windObject = weatherService.returnWind();
		double wind = windObject.getDouble("speed");
		
		//get sunrise and sunset
		JSONObject sysObject = weatherService.returnSunSet();
		long sunrise = sysObject.getLong("sunrise") * 1000;//needed to convert from unix to a normal time stamp
		long sunset = sysObject.getLong("sunset") * 1000;//needed to convert from unix to a normal time stamp
		
		//setup Icon image
		//extract an image as an External Resource from the API your calling
		String iconCode = null;
		String description = null;
		
		JSONArray jsonArray = weatherService.returnWeatherArray();
		for(int i = 0;i<jsonArray.length();i++) {
			JSONObject weatherObject = jsonArray.getJSONObject(i);
			
			iconCode = weatherObject.getString("icon");
			description = weatherObject.getString("description");
			
			//print for testing
			System.out.println(
					"id : " + weatherObject.getInt("id") + 
					", main: " + weatherObject.getString("main") + 
					", description: " + weatherObject.getString("description")
			);
		}
		
		//create an embedded version of the image, passing null as it's caption
		iconImage.setSource(new ExternalResource("http://openweathermap.org/img/w/" + iconCode +  ".png"));

		dashBoardMain.addComponents(currentLocationTitle,iconImage,currentTemp);
		mainLayout.addComponent(dashBoardMain);
		
		//update description UI
		weatherDescription.setValue("Cloudiness: " +  description);
		weatherDescription.addStyleName(ValoTheme.LABEL_SUCCESS);
		weatherMin.setValue("Min: " + String.valueOf(minTemp));
		weatherMax.setValue("Max: " + String.valueOf(maxTemp));
		pressureLabel.setValue("Pressure: " + String.valueOf(pressure) + " hpa");
		humidityLabel.setValue("Humidity: " + String.valueOf(humidity) + "%");
		windSpeedLabel.setValue("Wind: " + String.valueOf(wind) + " m/s");
		sunriseLabel.setValue("Sunrise: " + convertTime(sunrise));
		sunsetLabel.setValue("Sunset: " + convertTime(sunset));
		
		mainDescriptionLayout.addComponents(descriptionLayout,pressureLayout);
		mainLayout.addComponents(mainDescriptionLayout);
	}
	
	private String convertTime(long time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yy hh.mm aa");
		
		return dateFormat.format(new Date(time));
	}

//	private void showDescription() {
//		//description
//		//min temp
//		//max temp
//		
////		weatherDescription.setValue("Description: " + desc);
//	}


	




}