package org.hoongoin.interviewbank.account.application.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "spring.oauth2.client.registration.google")
@Data
public class GoogleOAuthProperties {
	private String authUri;
	private String tokenUri;
	private String clientId;
	private String clientSecret;
	private String redirectUri;
	private String profileUri;
	private List<String> scopes;
}