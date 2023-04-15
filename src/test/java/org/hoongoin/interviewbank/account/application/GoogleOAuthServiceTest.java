package org.hoongoin.interviewbank.account.application;

import static org.assertj.core.api.Assertions.*;

import org.hoongoin.interviewbank.config.IbSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;

@IbSpringBootTest
class GoogleOAuthServiceTest {

	@Autowired
	private GoogleOAuthService googleOAuthService;

	@Test
	void getGoogleLoginUri_Success() {
		//given
		String sessionId = "test";

		//when
		URI googleLoginUri = googleOAuthService.getGoogleLoginUri(sessionId);

		//then
		assertThat(googleLoginUri).isNotNull();
		System.out.println(googleLoginUri);
	}

}