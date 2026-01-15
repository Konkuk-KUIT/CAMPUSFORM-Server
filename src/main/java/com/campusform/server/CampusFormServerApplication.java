package com.campusform.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.campusform.server.identity.domain.model.User;
import com.campusform.server.identity.domain.repository.UserRepository;

@SpringBootApplication(exclude = {
		SecurityAutoConfiguration.class,
		UserDetailsServiceAutoConfiguration.class
})
@EnableJpaAuditing
public class CampusFormServerApplication {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(CampusFormServerApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		/**
		 * 테스트 데이터 세팅
		 */
		User user1 = User.create("iht@naver.com", "임형택", "test.url");
		userRepository.save(user1);
		User user2 = User.create("psg@naver.com", "박성근", "test.url");
		userRepository.save(user2);
		User user3 = User.create("cjw@naver.com", "최재원", "test.url");
		userRepository.save(user3);
		System.out.println("유저 세팅 완료");
	}
}
