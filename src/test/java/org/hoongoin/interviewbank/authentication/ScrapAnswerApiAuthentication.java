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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = {TestRedisConfig.class, TestMockConfig.class})
public class ScrapAnswerApiAuthentication {

	@Autowired
	private MockMvc mockMvc;

	@WithAnonymousUser
	@Test
	void updateScrapAnswer_Fail_Unauthorized() throws Exception {
		mockMvc.perform(put("/scraps/12/questions/4/answers/4"))
			.andExpect(status().isUnauthorized());
	}

	@IbWithMockUser
	@Test
	void updateScrapAnswer_Success() throws Exception {
		mockMvc.perform(put("/scraps/12/questions/4/answers/4"))
			.andExpect(status().isOk());
	}
}
