package org.hoongoin.interviewbank.interview.controller.request;

import lombok.Getter;

@Getter
public class CreateQuestionRequest {

	public CreateQuestionRequest(String content, long interviewId) {
		this.content = content;
		this.interviewId = interviewId;
	}

	private String content;
	private long interviewId;
}
