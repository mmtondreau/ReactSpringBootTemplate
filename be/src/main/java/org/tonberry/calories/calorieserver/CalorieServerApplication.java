package org.tonberry.calories.calorieserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableReactiveMethodSecurity
@SpringBootApplication
@EnableWebFlux
public class CalorieServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalorieServerApplication.class, args);
	}

}
