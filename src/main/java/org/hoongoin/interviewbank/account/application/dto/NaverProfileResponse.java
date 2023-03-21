package org.hoongoin.interviewbank.account.application.dto;

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
		private String profileImage;
		private String birthyear;
		private String mobile;
	}
}
