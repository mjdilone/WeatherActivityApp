package com.portfolio.weatherapp.WeatherApp.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.UI;

@SpringView(name= "about")
public class AboutView extends UI implements View {

	@Override
	public void enter(ViewChangeEvent event) {
		System.out.println("Enter into test view");
	}

	@Override
	protected void init(VaadinRequest request) {
		
	}

}
