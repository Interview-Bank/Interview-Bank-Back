package org.hoongoin.interviewbank.common.gpt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hoongoin.interviewbank.exception.IbInternalServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class GptRequestHandler {

    @Value("${gpt.uri}")
    private String gptUri;
    @Value("${gpt.model}")
    private String gptModel;
    @Value("${gpt.secret-key}")
    private String gptKey;
    private final RestTemplate restTemplate;

    public GptResponseBody sendChatCompletionRequest(List<GptRequestBody.Message> messages) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + gptKey);

        GptRequestBody gptRequestBody = new GptRequestBody(gptModel, messages);
        HttpEntity<GptRequestBody> entity = new HttpEntity<>(gptRequestBody, headers);

        ResponseEntity<GptResponseBody> response = restTemplate.exchange(gptUri, HttpMethod.POST, entity, GptResponseBody.class);
        if(response.getStatusCode() != HttpStatus.OK) {
            log.error("GPT request failed.");
            throw new IbInternalServerException("GPT request failed.");
        }
        return response.getBody();
    }
}
