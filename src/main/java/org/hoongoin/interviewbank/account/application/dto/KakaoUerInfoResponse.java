package org.hoongoin.interviewbank.account.application.dto;

import lombok.Getter;

@Getter
public class KakaoUerInfoResponse {

	private KakaoAccount kakaoAccount;

	@Getter
	public static class KakaoAccount {
		private String email;
		private Profile profile;

		@Getter
		public static class Profile {
			private String nickname;
			private String thumbnailImageUrl;
			private String profileImageUri;
			private String isDefaultImage;
		}
	}
}
