package org.hoongoin.interviewbank.common.gpt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageRole {

    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant");

    private final String role;
}
