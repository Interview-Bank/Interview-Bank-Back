package org.hoongoin.interviewbank.account.controller.response;

import lombok.Getter;

@Getter
public class UploadProfileImageResponse {

	private final String imageUrl;

	public UploadProfileImageResponse(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
