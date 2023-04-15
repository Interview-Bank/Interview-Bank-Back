package org.hoongoin.interviewbank.account.application.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "spring.oauth2.client.registration.kakao")
@Data
public class KakaoOAuthProperties {
	private String authUri;
	private String clientId;
	private String clientSecret;
	private String redirectUri;
	private String tokenUri;
	private String profileUri;
}
