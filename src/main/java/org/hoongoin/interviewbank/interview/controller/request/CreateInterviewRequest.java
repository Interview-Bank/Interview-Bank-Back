package org.hoongoin.interviewbank.interview.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateInterviewRequest {

	public CreateInterviewRequest(String title, long accountId) {
		this.title = title;
		this.accountId = accountId;
	}

	private String title;
	private long accountId;
}