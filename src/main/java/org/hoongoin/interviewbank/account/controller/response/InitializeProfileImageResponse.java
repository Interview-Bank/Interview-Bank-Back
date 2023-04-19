package org.hoongoin.interviewbank.account.controller.response;

import lombok.Getter;

@Getter
public class InitializeProfileImageResponse {

	private final String imageUrl;

	public InitializeProfileImageResponse(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
