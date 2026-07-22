package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Team9FinalProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(Team9FinalProjectApplication.class, args);
	}

}
