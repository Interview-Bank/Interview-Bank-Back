package org.hoongoin.interviewbank;

import org.hoongoin.interviewbank.account.application.dto.GoogleOAuthProperties;
import org.hoongoin.interviewbank.account.application.dto.KakaoOAuthProperties;
import org.hoongoin.interviewbank.account.application.dto.NaverOAuthProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties({GoogleOAuthProperties.class, KakaoOAuthProperties.class, NaverOAuthProperties.class})
public class InterviewbankApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterviewbankApplication.class, args);
	}

}
