package com.example.fiction_place1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FictionPlace1Application {

	public static void main(String[] args) {
		SpringApplication.run(FictionPlace1Application.class, args);
	}

}
