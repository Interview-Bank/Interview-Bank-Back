package org.hoongoin.interviewbank.account.application.dto;

import lombok.Getter;

@Getter
public class KakaoTokenResponse {

    private String tokenType;
    private String accessToken;
    private String idToken;
    private String expiresIn;
    private String refreshToken;
    private String refreshTokenExpiresIn;
    private String scope;
}
