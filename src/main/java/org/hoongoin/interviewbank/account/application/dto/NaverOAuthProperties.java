package org.hoongoin.interviewbank.account.application.dto;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "spring.oauth2.client.registration.naver")
@Data
public class NaverOAuthProperties {
	private String authUri;
	private String tokenUri;
	private String profileUri;
	private String clientId;
	private String clientSecret;
	private String redirectUri;
	private List<String> scopes;
}
