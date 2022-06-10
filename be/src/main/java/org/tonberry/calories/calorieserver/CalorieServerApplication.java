package org.tonberry.calories.calorieserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class CalorieServerApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(CalorieServerApplication.class, args);
	}

}
