package org.hoongoin.interviewbank.account.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class NaverTokenResponse {

	@JsonProperty("access_token")
	private String accessToken;
	@JsonProperty("refresh_token")
	private String refreshToken;
	@JsonProperty("token_type")
	private String tokenType;
	@JsonProperty("expires_in")
	private String expiresIn;
	private String error;
	@JsonProperty("error_description")
	private String errorDescription;
}
