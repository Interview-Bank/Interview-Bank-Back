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
class InterviewApiAuthentication {

    @Autowired
    private MockMvc mockMvc;

    @WithAnonymousUser
    @Test
    void createInterviewAndQuestions_Fail_Unauthorized() throws Exception {
        mockMvc.perform(post("/interview"))
                .andExpect(status().isUnauthorized());
    }

    @IbWithMockUser
    @Test
    void createInterviewAndQuestions_Success() throws Exception {
        mockMvc.perform(post("/interview"))
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void findInterviewPage_Success() throws Exception {
        mockMvc.perform(get("/interview"))
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void updateInterview_Fail_Unauthorized() throws Exception {
        mockMvc.perform(put("/interview/10"))
                .andExpect(status().isUnauthorized());
    }

    @IbWithMockUser
    @Test
    void updateInterview_Success() throws Exception {
        mockMvc.perform(put("/interview/10"))
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void deleteInterview_Fail_Unauthorized() throws Exception {
        mockMvc.perform(delete("/interview/10"))
                .andExpect(status().isUnauthorized());
    }

    @IbWithMockUser
    @Test
    void deleteInterview_Success() throws Exception {
        mockMvc.perform(delete("/interview/10"))
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void getInterview_Success() throws Exception {
        mockMvc.perform(get("/interview/10"))
                .andExpect(status().isOk());
    }
}
