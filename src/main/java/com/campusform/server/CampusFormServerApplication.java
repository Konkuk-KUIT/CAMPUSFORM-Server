package com.campusform.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync // 이 설정이 있어야 비동기 처리가 활성화됩니다.
@SpringBootApplication(exclude = { // 시큐리티 비활성화 for API 테스트
		SecurityAutoConfiguration.class,
		UserDetailsServiceAutoConfiguration.class
})
@EnableJpaAuditing
public class CampusFormServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CampusFormServerApplication.class, args);
	}
}
