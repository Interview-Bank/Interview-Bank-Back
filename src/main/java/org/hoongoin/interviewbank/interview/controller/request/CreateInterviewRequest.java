package org.hoongoin.interviewbank.interview.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateInterviewRequest {

	private String title;
	private long accountId;
}