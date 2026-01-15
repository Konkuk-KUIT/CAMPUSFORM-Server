package com.campusform.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableJpaAuditing
public class CampusFormServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CampusFormServerApplication.class, args);
	}

	@PostConstruct
	public void init() {
		System.out.println("==== 서버 시작 ====");
	}
}
