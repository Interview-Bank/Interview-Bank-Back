package org.hoongoin.interviewbank.authentication;

import org.hoongoin.interviewbank.config.IbWithMockUser;
import org.hoongoin.interviewbank.config.TestMockConfig;
import org.hoongoin.interviewbank.config.TestRedisConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = {TestRedisConfig.class, TestMockConfig.class})
class AccountApiAuthentication {

	@Autowired
	private MockMvc mockMvc;

	@WithAnonymousUser
	@Test
	void logout_Fail_Unauthorized() throws Exception {
		mockMvc.perform(post("/account/logout"))
			.andExpect(status().isUnauthorized());
	}

	@IbWithMockUser
	@Test
	void logout_Success() throws Exception {
		mockMvc.perform(post("/account/logout"))
			.andExpect(status().isOk());
	}

	@WithAnonymousUser
	@Test
	void register_Success() throws Exception {
		mockMvc.perform(post("/account/register"))
			.andExpect(status().isOk());
	}

	@WithAnonymousUser
	@Test
	void login_Success() throws Exception {
		mockMvc.perform(post("/account/login"))
			.andExpect(status().isOk());
	}

	@WithAnonymousUser
	@Test
	void sendEmailToResetPassword_Success() throws Exception {
		mockMvc.perform(post("/account/reset-password/send-email"))
			.andExpect(status().isOk());
	}

	@WithAnonymousUser
	@Test
	void resetPasswordTokenValid_Success() throws Exception {
		mockMvc.perform(post("/account/reset-password/token-validation"))
			.andExpect(status().isOk());
	}

	@WithAnonymousUser
	@Test
	void resetPassword_Success() throws Exception {
		mockMvc.perform(post("/account/reset-password"))
			.andExpect(status().isOk());
	}

	@WithAnonymousUser
	@Test
	void getMe_Fail_Unauthorized() throws Exception {
		mockMvc.perform(post("/account/me"))
			.andExpect(status().isUnauthorized());
	}

	@IbWithMockUser
	@Test
	void getMe_Success() throws Exception {
		mockMvc.perform(post("/account/me"))
			.andExpect(status().isOk());
	}
}
