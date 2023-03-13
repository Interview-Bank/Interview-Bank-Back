package org.hoongoin.interviewbank.account.application.dto;

import lombok.Builder;

@Builder
public class GoogleTokenRequestParams {

    private String clientId;
    private String clientSecret;
    private String code;
    private String grantType;
    private String redirectUri;
}
