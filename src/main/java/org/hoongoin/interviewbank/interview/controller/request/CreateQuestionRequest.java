package org.hoongoin.interviewbank.interview.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateQuestionRequest {

	private String content;
	private long interviewId;
}