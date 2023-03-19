package org.hoongoin.interviewbank.account.controller.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;

@Getter
public class UploadProfileImageRequest {

	private final MultipartFile multipartFile;

	public UploadProfileImageRequest(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}
}
