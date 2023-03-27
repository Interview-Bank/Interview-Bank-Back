package org.hoongoin.interviewbank.interview.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateInterviewAndQuestionsRequest {

	private String title;
	private String primaryJobCategory;
	private String secondaryJobCategory;
	private QuestionsRequest questionsRequest;
}
