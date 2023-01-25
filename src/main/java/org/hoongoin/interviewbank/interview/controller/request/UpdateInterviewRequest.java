package org.hoongoin.interviewbank.interview.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInterviewRequest {

	public UpdateInterviewRequest(String title) {
		this.title = title;
	}

	private String title;
}
