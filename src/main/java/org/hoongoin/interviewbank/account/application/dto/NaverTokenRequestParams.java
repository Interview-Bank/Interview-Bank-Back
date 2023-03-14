package org.hoongoin.interviewbank.account.application.dto;

import lombok.Builder;

@Builder
public class NaverTokenRequestParams {

	private String grantType;
	private String clientId;
	private String clientSecret;
	private String code;
	private String state;
	private String refreshToken;
	private String accessToken;
	private String serviceProvider;
}
