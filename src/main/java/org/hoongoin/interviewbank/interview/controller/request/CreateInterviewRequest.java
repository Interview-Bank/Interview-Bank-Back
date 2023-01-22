package org.hoongoin.interviewbank.interview.controller.request;

import lombok.Getter;

@Getter
public class CreateInterviewRequest {

	public CreateInterviewRequest(String title, long accountId) {
		this.title = title;
		this.accountId = accountId;
	}

	private String title;
	private long accountId;
}
