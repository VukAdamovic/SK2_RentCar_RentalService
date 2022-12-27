package com.example.SK_Project2.RentalCarService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RentalCarServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentalCarServiceApplication.class, args);
	}

}
