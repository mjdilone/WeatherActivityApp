package com.portfolio.weatherapp.WeatherApp.view;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.portfolio.weatherapp.WeatherApp.controller.ActiveService;
import com.portfolio.weatherapp.WeatherApp.controller.Constants;
import com.portfolio.weatherapp.WeatherApp.controller.Utils;
import com.portfolio.weatherapp.WeatherApp.controller.WeatherService;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
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
	
	Navigator navigator;
	
	
	private String slideInUp = "animated slideInUp";
	private String slideInUpSlow = "animated slideInUp";
	private String slideOutUp = "animated slideOutUp";
	
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
	private HorizontalLayout activityLayout;
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
	
	//init starts up the page with empty elements and then a listener waits for input
	@Override
	protected void init(VaadinRequest request) {
		
		//testing
		// menu toggle
		
		//end testing
		
		topLayout = new VerticalLayout();
		topLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		
	
		//sets up all widgets and componenets that will be used. In a way it creates nothing but insantiates the widgets
		//This avoids an issue where some elements will be null before they can be added since they're instantiated in other code blocks
		setUpLayout();
		//Creates the header for the page then adds it to mainLayout
		setHeader();
		//creates the logo for the page using an internall image
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
		showActivityButton.setStyleName(slideInUp);
		
		//testing
//		mainLayout.setHeight("98%");
		
		
		
		//end testing
		
		//this is a CLICK listener, so it's called with a click and then checks the value of cityTextField
		showWeatherButton.addClickListener(event0 -> {
			topLayout.setStyleName(slideOutUp);
			
			refreshUI();
		});
		
		showWeatherButton.addClickListener(event1 -> {
			if(cityTextField.getValue().equals("") ) {
				//Notification.show("Please enter a city");
				cityTextField.setValue(Constants.testWeatherCityInput);
			}else {
					mainLayout.removeComponent(topLayout);
//					mainLayout.removeComponent(headerLayout);
//					mainLayout.setHeight(Double.toString(Page.getCurrent().getBrowserWindowHeight() * .5));
//					mainLayout.setSpacing(disabled);
					mainLayout.setSizeFull();
//					mainLayout.setStyleName("animated noscroll");
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
		
		logoImage = new Image("",new FileResource(new File("src/main/resources/img/icons/77_Essential_Icons_Location Marker.png")));
		logoLayout.addComponent(logoImage);

		topLayout.addComponent(logoLayout);
		mainLayout.addComponent(topLayout);
	}

	private void setUpForm() {
		formLayout = new HorizontalLayout();
		formLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		formLayout.setSpacing(enabled);
		//formLayout.setMargin(enabled);
		
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
		//creates a nice Vaadin text field
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
		
		
		//handmade timer
//		Long currentTime = System.currentTimeMillis();
//		Long endTime = currentTime + 2000;
//		while(System.currentTimeMillis() <= endTime) {
//		}
	}
	
	
	private void refreshUI() {
		Page.getCurrent();
	}

	private void updateWeatherUI(){
		//sets the location title to what's been searched in the city search bar
		currentLocationTitle.setValue("Currently in " + cityTextField.getValue() + ":");
		
//		try {
//		    TimeUnit.SECONDS.sleep(1);
//		} catch (InterruptedException ie) {
//		    Thread.currentThread().interrupt();
//		}
		


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
		
		//takes all the information that's been gathered and applies them to all the empty labels that were made earlier
		weatherDescription.setValue("Cloudiness: " +  description);
		weatherDescription.addStyleName(ValoTheme.LABEL_SUCCESS);
		weatherMin.setValue("Min: " + String.valueOf(minTemp));
		weatherMax.setValue("Max: " + String.valueOf(maxTemp));
		pressureLabel.setValue("Pressure: " + String.valueOf(pressure) + " hpa");
		humidityLabel.setValue("Humidity: " + String.valueOf(humidity) + "%");
		windSpeedLabel.setValue("Wind: " + String.valueOf(wind) + " m/s");
		sunriseLabel.setValue("Sunrise: " + Utils.convertTime(sunrise));
		
		sunsetLabel.setValue("Sunset: " + Utils.convertTime(sunset));
		
		//add the activity button
		showActivityButton.setIcon(VaadinIcons.CAR);
		activityPromptLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		activityPromptLayout.addComponent(showActivityButton);
		
		//add the elements to the main container that holds descriptions
		mainDescriptionLayout.addComponents(descriptionLayout,pressureLayout);
		//adds the description to the main container that holds anything
		mainLayout.addComponents(mainDescriptionLayout,activityPromptLayout);
	}
	
	private void updateActivityUI() {
	    
	    //testing
	    mainLayout.removeComponent(mainDescriptionLayout);
//	    mainDescriptionLayout.setVisible(disabled);
	    //end testing
	    
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
	    ArrayList<String> list = new ArrayList<>();
	    
	    try {
	      activityDescription = (activityResultsJSONArray.getJSONObject(0).getJSONArray("assetDescriptions").getJSONObject(0).get("description")).toString();
	      activityURL = (activityResultsJSONArray.getJSONObject(0).get("homePageUrlAdr")).toString();
	      activityLocation = (activityResultsJSONArray.getJSONObject(0).getJSONObject("place")).get("addressLine1Txt").toString();
	      activityStartDate = (activityResultsJSONArray.getJSONObject(0).get("activityStartDate")).toString();
	      activityLogoImageURL = (activityResultsJSONArray.getJSONObject(0).get("logoUrlAdr")).toString();
	      activityName = ((activityResultsJSONArray.getJSONObject(0).get("assetName").toString()));
	      list.add(activityDescription);
	      list.add(activityURL);
	      list.add(activityLocation);
	      list.add(activityStartDate);
	      list.add(activityLogoImageURL);
	      list.add(activityName);
	      Utils.printAll(list);
	      
	    } catch (JSONException e) {
	      e.printStackTrace();
	    }
	    
	    //testing
	    activityDisplayLayout = new HorizontalLayout();
	    activityDisplayLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
	    //end testing
	    
	    Panel activityInfoPanel = new Panel(activityDescription);
	    Panel activityInfoPanel1 = new Panel(Utils.lorem);
	    Panel activityInfoPanel2 = new Panel(Utils.lorem);
	    
	    //test panel, try adding a textfield
	    Label testLabel = new Label(activityDescription,ContentMode.HTML);
//	    testLabel.setSizeUndefined();
	    testLabel.setWidth("1000px");
//	    testLabel.setHeight("300px");
//	    testLabel.setValue(activityDescription);
	   
	    
	    //testing Panel that holds a layout, the layout itself is then scrollable
	    VerticalLayout testLayout = new VerticalLayout();
	    testLayout.addComponent(testLabel);
	    Panel testPanel2 = new Panel();
	    testPanel2.setHeight("250px");
	    System.out.println(Page.getCurrent().getBrowserWindowWidth());
	    double browserPageWidth = Page.getCurrent().getBrowserWindowWidth();
	    String panelWidth = Double.toString(browserPageWidth * .92);
	    testPanel2.setWidth(panelWidth);
	    
	    testPanel2.setCaption("test2");
	    testPanel2.setContent(testLayout);
	    
	    //end testPanel
	    
	    //testing
	    ArrayList<Panel> panelList = new ArrayList<Panel>();
	    panelList.add(activityInfoPanel);
	    panelList.add(activityInfoPanel1);
	    panelList.add(activityInfoPanel2);
	    panelList.add(testPanel2);
	    
	    //testing
	    TabSheet tabsheet = new TabSheet();
	    tabsheet.addStyleName(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS );
	    int tabCounter = 0;
	    
	    for(Panel panel :panelList) {
	      panel.addStyleName("animated slideInRight ");
//	      panel.setWidth("100%");
//	      panel.setHeight("100%");
	      VerticalLayout tabToAdd = new VerticalLayout(panel);
	      tabToAdd.setCaption("tab " + tabCounter++);
	      tabsheet.addTab(tabToAdd);
	    }

	    activityDisplayLayout.addComponent(tabsheet);
	    //end testing

	    activityDisplayLayout.setVisible(enabled);
	    mainLayout.addComponent(activityDisplayLayout);
	    //add a class to a component as if it were an HTML element
	    activityInfoPanel.addStyleName("animated slideInRight");
	  }
}