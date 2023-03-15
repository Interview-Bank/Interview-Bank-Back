package org.hoongoin.interviewbank.account.application.dto;

import lombok.Builder;

@Builder
public class KakaoTokenRequestParams {

	private String grantType;
	private String clientId;
	private String redirectUri;
	private String code;
	private String clientSecret;
}
