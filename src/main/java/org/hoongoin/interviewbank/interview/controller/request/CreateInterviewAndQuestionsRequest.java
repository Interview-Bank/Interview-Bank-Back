package org.hoongoin.interviewbank.interview.controller.request;

import org.hoongoin.interviewbank.interview.enums.CareerYear;
import org.hoongoin.interviewbank.interview.enums.InterviewPeriod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateInterviewAndQuestionsRequest {

	private String title;
	private Long jobCategoryId;
	private InterviewPeriod interviewPeriod;
	private CareerYear careerYear;
	private QuestionsRequest questionsRequest;
}
