package org.hoongoin.interviewbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableJpaAuditing
public class InterviewbankApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterviewbankApplication.class, args);
	}

}
