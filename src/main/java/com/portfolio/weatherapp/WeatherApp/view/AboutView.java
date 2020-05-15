package com.portfolio.weatherapp.WeatherApp.view;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.ui.UI;

@SpringUI(path= "about")
public class AboutView extends UI implements View {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5299834423471961058L;
	Navigator navigator;
    protected static final String MAINVIEW = "";

	@Override
	public void enter(ViewChangeEvent event) {
		System.out.println("Enter into test view");
	}

	@Override
	protected void init(VaadinRequest request) {
//		getPage().setTitle("Navigation Example");

        // Create a navigator to control the views
//        navigator = new Navigator(this, this);

        // Create and register the views
//        navigator.addView(MAINVIEW, new MainView());
//        navigator.navigateTo(MAINVIEW);
//        navigator.navigateTo("");
//        SpringNavigator navi = new SpringNavigator();
//        navi.navigateTo("main");
//        getUI().getNavigator().navigateTo(MAINVIEW);

	}

}
