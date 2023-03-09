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
class ScrapApiAuthentication {

    @Autowired
    private MockMvc mockMvc;

    @WithAnonymousUser
    @Test
    void createScrap_Fail_Unauthorized() throws Exception{
        mockMvc.perform(post("/scraps"))
                .andExpect(status().isUnauthorized());
    }

    @IbWithMockUser
    @Test
    void createScrap_Success() throws Exception{
        mockMvc.perform(post("/scraps"))
                .andExpect(status().isOk());
    }
}




