package org.hoongoin.interviewbank.interview.controller.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInterviewResponse {

	public UpdateInterviewResponse(String title) {
		this.title = title;
	}

	String title;
}
