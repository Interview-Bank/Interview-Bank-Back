package org.hoongoin.interviewbank.gpt;

import org.hoongoin.interviewbank.common.gpt.GptResponseBody;

import java.util.List;

public class GptResponseBodyFactory {

    public static GptResponseBody createMockGptResponseBody() {
        GptResponseBody.Choice choice = new GptResponseBody.Choice();
        GptResponseBody.Choice.Message message = new GptResponseBody.Choice.Message();
        message.setContent("content");
        choice.setMessage(message);
        GptResponseBody gptResponseBody = new GptResponseBody();
        gptResponseBody.setChoices(List.of(choice));
        return gptResponseBody;
    }
}
