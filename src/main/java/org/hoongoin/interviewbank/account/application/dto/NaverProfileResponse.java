package org.hoongoin.interviewbank.account.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class NaverProfileResponse {

	private String resultcode;
	private String message;
	private Response response;

	@Getter
	public static class Response {
		private String id;
		private String nickname;
		private String name;
		private String email;
		private String gender;
		private String age;
		private String birthdate;
		@JsonProperty("profile_image")
		private String profileImage;
		private String birthyear;
		private String mobile;
	}
}
