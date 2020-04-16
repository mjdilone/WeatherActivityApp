package com.portfolio.weatherapp.WeatherApp.view;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.portfolio.weatherapp.WeatherApp.controller.ActiveService;
import com.portfolio.weatherapp.WeatherApp.controller.Constants;
import com.portfolio.weatherapp.WeatherApp.controller.Utils;
import com.portfolio.weatherapp.WeatherApp.controller.WeatherService;
import com.portfolio.weatherapp.WeatherApp.model.Activity;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI(path = "")
@StyleSheet("vaadin://animate.css")
public class MainView extends UI{
	private static final long serialVersionUID = 1L;

	@Autowired
	private WeatherService weatherService;
	
	@Autowired 
	ActiveService activeService;
	
	private boolean enabled = true;
	private boolean disabled = false;

	//weather elements
	private Image iconImage;
	private Image logoImage;
	
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
	
	//activity elements
	private Button showActivityButton;
	private Label activityLabel;
	
	//verticalLayout elements go up and down
	private VerticalLayout mainLayout;
	private VerticalLayout descriptionLayout;
	private VerticalLayout pressureLayout;
	private VerticalLayout activityPromptLayout;

	//horizontal elements go left to right
	private HorizontalLayout formLayout;
	private HorizontalLayout headerLayout;
	private HorizontalLayout logoLayout;
	private VerticalLayout topLayout;

	private HorizontalLayout dashBoardMain;
	private HorizontalLayout mainDescriptionLayout;
	
	private HorizontalLayout activityDisplayLayout;
	private TabSheet activityTabSheet;
	private VerticalLayout activityTabSheetLayout;
	private VerticalLayout tab;
	private Panel activityPanel;
	
	//init starts up the page with empty elements and then a listener waits for input
	@Override
	protected void init(VaadinRequest request) {
		topLayout = new VerticalLayout();
		topLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

		//sets up all widgets and componenets that will be used. In a way it creates nothing in the UI, but insantiates the widgets
		//This avoids an issue where some elements will be null before they can be added since they're instantiated in other methodsh
		setUpLayout();
		//Creates the header for the page then adds it to mainLayout
		setHeader();
		//creates the logo for the page using an internal image
		setLogo();
		//sets up the form that will recieve the info needed from the user
		setUpForm();
		//creates an empty invisible dashboard that will take in your results from the API
		dashBoardTitle();
		//adds empty containers for the dashboard, these will then be populated
		dashBoardDescription();

		topLayout.setStyleName("animated slideInUp");
		dashBoardMain.setStyleName("animated slideInUp");
		mainDescriptionLayout.setStyleName("animated slideInUp");
		showActivityButton.setStyleName(Constants.slideInUp);
		
		mainLayout.setStyleName("animated wallpaper ");		
		mainLayout.setSizeFull();
		
		//binds the click listener to the enter button
		showWeatherButton.setClickShortcut(KeyCode.ENTER);
		//this is a CLICK listener, so it's called with a click and then checks the value of cityTextField
		showWeatherButton.addClickListener(event0 -> {
			topLayout.setStyleName(Constants.slideInUp);
			refreshUI();
		});
		
		showWeatherButton.addClickListener(event1 -> {
			if(cityTextField.getValue().equals("") ) {
				//Notification.show("Please enter a city");
				cityTextField.setValue(Constants.testWeatherCityInput);
			}else {
					mainLayout.removeComponent(topLayout);
					updateUI();
			}
		});
	
		showActivityButton.addClickListener(event -> {
			updateActivityUI();
		    mainLayout.setExpandRatio(activityDisplayLayout,1.0f);
		});		
	}
	
	public MainView() {
	}
	
	public void setUpLayout() {
		iconImage = new Image();
		
		//instantiating all the layouts of the page, they have to be instantiated here so that the results of the last page don't show for every new search
		mainDescriptionLayout = new HorizontalLayout();
		descriptionLayout = new VerticalLayout();
		pressureLayout = new VerticalLayout();
		activityPromptLayout = new VerticalLayout();

		//weather
		weatherDescription = new Label();
		weatherMin = new Label();
		weatherMax = new Label();
		pressureLabel = new Label();
		humidityLabel = new Label();
		windSpeedLabel = new Label();
		sunriseLabel = new Label();
		sunsetLabel = new Label();
		
		//activity 
		showActivityButton = new Button();
	    activityDisplayLayout = new HorizontalLayout();
	    activityTabSheet = new TabSheet();
		
		//mainLayout 
		mainLayout = new VerticalLayout();
		mainLayout.setWidth("100%");//allows the layout to work with the entire screen
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		setContent(mainLayout);
	}
	
	public void setHeader() {
		headerLayout = new HorizontalLayout();
		headerLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		Label title = new Label("Weather");
		
		//adding prebuilt styling to the label called title
		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_BOLD);
		title.addStyleName(ValoTheme.LABEL_COLORED);
		
		//adding the title to the headerlayout, which is the surrounding container
		headerLayout.addComponents(title);
		
		//add the headerLayout to the main layout
		topLayout.addComponent(headerLayout);
		mainLayout.addComponents(topLayout);
	}
	
	private void setLogo() {
		logoLayout = new HorizontalLayout();		
		logoLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		logoImage = new Image("",new FileResource(new File(Constants.locationMarkerFileLocation)));
		logoLayout.addComponent(logoImage);

		topLayout.addComponent(logoLayout);
		mainLayout.addComponent(topLayout);
	}

	private void setUpForm() {
		formLayout = new HorizontalLayout();
		formLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		formLayout.setSpacing(enabled);
		formLayout.setMargin(enabled);
		
		//create the selection component that will allow you to pick between C and F
		unitSelect = new NativeSelect<>();
		unitSelect.setWidth("30px");
		
		//creating an ordinary array list that holds the elements 
		ArrayList<String> items = new ArrayList<>();
		items.add("F");
		items.add("C");
		
		//setting the arrayList as the thing that the unitSelect will hold
		unitSelect.setItems(items);
		unitSelect.setValue(items.get(0));
		
		//finally, add it to the formLayout
		formLayout.addComponent(unitSelect);
		
		//add textField
		cityTextField = new TextField();
		cityTextField.setWidth("40%");
		formLayout.addComponents(cityTextField);
		
		//add a Vaadin button to the form
		showWeatherButton = new Button();
		showWeatherButton.setIcon(VaadinIcons.SEARCH);
		formLayout.addComponents(showWeatherButton);
		
		topLayout.addComponent(formLayout);
		mainLayout.addComponents(topLayout);
	}
	
	private void dashBoardTitle() {
		dashBoardMain = new HorizontalLayout();
		dashBoardMain.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		currentLocationTitle = new Label();
		currentLocationTitle.addStyleName(ValoTheme.LABEL_H2);
		currentLocationTitle.addStyleName(ValoTheme.LABEL_LIGHT);
		
		//current temp label
		currentTemp = new Label();
		currentTemp.addStyleName(ValoTheme.LABEL_BOLD);
		currentTemp.addStyleName(ValoTheme.LABEL_H1);
		
	}
	
	private void dashBoardDescription() {
		mainDescriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		//Description Vertical Layout
		descriptionLayout.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		descriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		//these values are still empty, but they are added to the description layout nonetheless
		descriptionLayout.addComponents(weatherDescription);
		descriptionLayout.addComponents(weatherMin);
		descriptionLayout.addComponents(weatherMax);
		
		//Pressure humidity etc, empty but added now
		pressureLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		pressureLayout.addComponent(pressureLabel);
		pressureLayout.addComponent(humidityLabel);
		pressureLayout.addComponent(windSpeedLabel);
		pressureLayout.addComponent(sunriseLabel);
		pressureLayout.addComponent(sunsetLabel);
	}

	private void updateUI() {
		refreshUI();
		updateWeatherUI();
	}
	
	private void refreshUI() {
		//empty for now
	}

	private void updateWeatherUI(){
		//sets the location title to what's been searched in the city search bar
		currentLocationTitle.setValue("Currently in " + cityTextField.getValue() + ":");
		currentLocationTitle.addStyleName("animated bold");

		JSONObject mainObject = new JSONObject();
		String city = cityTextField.getValue();
		String defaultUnit;
		String unit;
		
		//setting up a string based on what option was picked in the unitSelect
		if(unitSelect.getValue().equals("F")) {
			defaultUnit = "imperial";	
			unitSelect.setValue("F");
			unit = "\u00b0" + "F";
		}else {
			defaultUnit = "metric";
			unitSelect.setValue("C");
			unit = "\u00b0" + "C";
		}
		
		//setters for the weatherService, just setting up some values for the API request
		weatherService.setCityName(city);
		weatherService.setUnit(defaultUnit);
		
		try {
			//calling the weather service 
			mainObject = weatherService.returnMain();
		} catch (Exception e) {
			System.out.println("Weather Service has failed");
			e.printStackTrace();
		}
		
		//getting the temperature from the main object, specifically it goes into the JSON Object's array and looks for an Int with the name of "temp"
		//the name of the element must exactly match the name from the JSON object, which is the response from the weather API
		int temp = mainObject.getInt("temp");
		//adding the value of the temp variable to the currentTemp label
		currentTemp.setValue(temp + "F");
		
		//get min,max,pressure,humidity from the JSON object, just like with the temperature
		double minTemp = mainObject.getDouble("temp_min");
		double maxTemp = mainObject.getDouble("temp_max");
		int pressure = mainObject.getInt("pressure");
		int humidity = mainObject.getInt("humidity");
		
		//get wind speed, which is a different JSON object from main
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
		
		//return a small array that contains some info about the weather
		JSONArray jsonArray = weatherService.returnWeatherArray();
		for(int i = 0;i<jsonArray.length();i++) {
			JSONObject weatherObject = jsonArray.getJSONObject(i);
			
			iconCode = weatherObject.getString("icon");
			description = weatherObject.getString("description");
			
			//print weather details
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
		
		//takes all the information that's been gathered and applies them to all the empty labels that were made earlier
		weatherDescription.setValue("Cloudiness: " +  description);
		weatherDescription.setStyleName("animated bold");
		weatherMin.setValue("Min: " + String.valueOf(minTemp));
		weatherMin.setStyleName("animated bold");
		weatherMax.setValue("Max: " + String.valueOf(maxTemp));
		weatherMax.setStyleName("animated bold");
		pressureLabel.setValue("Pressure: " + String.valueOf(pressure) + " hpa");
		pressureLabel.setStyleName("animated bold");
		humidityLabel.setValue("Humidity: " + String.valueOf(humidity) + "%");
		humidityLabel.addStyleName("animated bold");
		windSpeedLabel.setValue("Wind: " + String.valueOf(wind) + " m/s");
		windSpeedLabel.addStyleName("animated bold");
		sunriseLabel.setValue("Sunrise: " + Utils.convertTime(sunrise));
		sunriseLabel.addStyleName("animated bold");
		sunsetLabel.setValue("Sunset: " + Utils.convertTime(sunset));
		sunsetLabel.addStyleName("animated bold");
		
		//add the activity button
		showActivityButton.setIcon(VaadinIcons.CAR);
		activityPromptLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		//set click listener to show activity button
		showActivityButton.setClickShortcut(KeyCode.ENTER);
		activityPromptLayout.addComponent(showActivityButton);
		
		//add the elements to the main container that holds descriptions
		mainDescriptionLayout.addComponents(descriptionLayout,pressureLayout);
		//adds the description to the main container that holds anything
		mainLayout.addComponents(mainDescriptionLayout,activityPromptLayout);
	}
	
	private void updateActivityUI() {
		//remove the description layout to make room for the activity tabsheet
	    mainLayout.removeComponent(mainDescriptionLayout);

	    //JSON array to hold all activities around the given location
	    JSONArray activityResultsJSONArray = new JSONArray();
	    try {
	      int resultSize = Integer.parseInt(activeService.fetchActivityList(cityTextField.getValue()).get("total_results").toString());
	      System.out.println("Result Size: " + resultSize);
	      activityResultsJSONArray = activeService.fetchActivityList(cityTextField.getValue()).getJSONArray("results");
	    } catch (Exception e) {
	      System.out.println("fetching the activity list has failed");
	      System.out.println(e.getMessage());
	    }

	    String activityDescription = null;
	    String activityURL = null;
	    String activityLocation = null;
	    String activityStartDate = null;
	    String activityLogoImageURL = null;
	    String activityName = null;
	    
	    //making the list of the first 5 activities
	    ArrayList<Activity> activityList = new ArrayList<>();
	    for(int i =0;i<=5;i++) {
	        activityDescription = (activityResultsJSONArray.getJSONObject(i).getJSONArray("assetDescriptions").getJSONObject(0).get("description")).toString();
		    activityURL = (activityResultsJSONArray.getJSONObject(i).get("homePageUrlAdr")).toString();
		    activityLocation = (activityResultsJSONArray.getJSONObject(i).getJSONObject("place")).get("addressLine1Txt").toString();
		    activityStartDate = (activityResultsJSONArray.getJSONObject(i).get("activityStartDate")).toString();
		    activityLogoImageURL = (activityResultsJSONArray.getJSONObject(i).get("logoUrlAdr")).toString();
		    activityName = ((activityResultsJSONArray.getJSONObject(i).get("assetName").toString()));
		    
		    Activity activity = new Activity(activityDescription, activityURL, activityLocation, activityStartDate, activityLogoImageURL,activityName);
		    activityList.add(activity);
	    }
	    
	    //setting up the activity display layout
	    mainLayout.removeComponent(activityDisplayLayout);//TODO change the code here when you start implementing page functionality
	    activityDisplayLayout = new HorizontalLayout();
	    activityDisplayLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
	    
	    activityTabSheet = createActivityTabSheet(activityList);
	    activityTabSheet.addStyleName( ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);
	    
	    activityDisplayLayout.addComponent(activityTabSheet);
	    activityDisplayLayout.setVisible(enabled);
	    
	    mainLayout.addComponent(activityDisplayLayout);
	  }
	
	//implements TabSheet creation
	public TabSheet createActivityTabSheet(ArrayList<Activity> list) {
		activityTabSheet = new TabSheet();
		
		for(Activity activity: list) {
			activityLabel = new Label(activity.getDescription(),ContentMode.HTML);
			activityLabel.setWidth("1000px");//TODO hard coded string
			
			activityTabSheetLayout = new VerticalLayout();
			activityTabSheetLayout.addComponent(activityLabel);
			
			activityPanel = new Panel();
			activityPanel.setHeight("250px");
			activityPanel.addStyleName("animated slideInRight");//TODO hard coded string
			
		    activityPanel.setWidth(Constants.getPanelWidth());
		    activityPanel.setContent(activityTabSheetLayout);
		    
		    tab = new VerticalLayout(activityPanel);
		    tab.setCaption(activity.getName());
		    activityTabSheet.addTab(tab);
		}
		return activityTabSheet;
	}
}