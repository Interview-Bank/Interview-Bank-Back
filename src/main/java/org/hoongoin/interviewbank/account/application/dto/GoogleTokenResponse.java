package org.hoongoin.interviewbank.account.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoogleTokenResponse {
	private String accessToken;
	private String expiresIn;
	private String refreshToken;
	private String scope;
	private String tokenType;
}
