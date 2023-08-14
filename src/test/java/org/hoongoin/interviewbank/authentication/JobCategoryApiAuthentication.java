package org.hoongoin.interviewbank.authentication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hoongoin.interviewbank.config.TestMockConfig;
import org.hoongoin.interviewbank.config.TestRedisConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest(classes = {TestRedisConfig.class, TestMockConfig.class})
public class JobCategoryApiAuthentication {

	@Autowired
	private MockMvc mockMvc;

	@WithAnonymousUser
	@Test
	void getSecondLevelJobCategories_Success_Unauthorized() throws Exception {
		mockMvc.perform(get("/job-categories/second-level"))
			.andExpect(status().isOk());
	}
}
