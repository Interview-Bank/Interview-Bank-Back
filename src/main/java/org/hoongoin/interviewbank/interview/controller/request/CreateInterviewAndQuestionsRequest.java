package org.hoongoin.interviewbank.interview.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateInterviewAndQuestionsRequest {

	public CreateInterviewAndQuestionsRequest(String title, Long accountId,
		QuestionsRequest questionsRequest) {
		this.title = title;
		this.accountId = accountId;
		this.questionsRequest = questionsRequest;
	}

	private String title;
	private Long accountId;
	private QuestionsRequest questionsRequest;
}
