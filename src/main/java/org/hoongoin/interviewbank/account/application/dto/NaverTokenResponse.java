package org.hoongoin.interviewbank.account.application.dto;

import lombok.Getter;

@Getter
public class NaverTokenResponse {

	private String accessToken;
	private String refreshToken;
	private String tokenType;
	private String expiresIn;
	private String error;
	private String errorDescription;
}
