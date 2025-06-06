package com.example.fluxkart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FluxkartApplication {

	public static void main(String[] args) {
		SpringApplication.run(FluxkartApplication.class, args);
		System.out.println("welcome to fluxkart");
		System.out.println("DATASOURCE_URL = " + System.getenv("DATASOURCE_URL"));
		System.out.println("DATASOURCE_USER = " + System.getenv("DATASOURCE_USER"));
		System.out.println("DATASOURCE_PASSWORD = " + System.getenv("DATASOURCE_PASSWORD"));

	}

}
