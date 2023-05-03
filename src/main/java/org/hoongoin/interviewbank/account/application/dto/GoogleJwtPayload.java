package org.hoongoin.interviewbank.account.application.dto;

import lombok.Getter;

@Getter
public class GoogleJwtPayload {
	private String email;
	private String nonce;
	private String name;
	private String picture;
}
