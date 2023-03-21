package org.hoongoin.interviewbank.account.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoogleTokenResponse {
	private String accessToken;
	private String expiresIn;
	private String idToken;
	private String scope;
	private String tokenType;
	private String refreshToken;
}
