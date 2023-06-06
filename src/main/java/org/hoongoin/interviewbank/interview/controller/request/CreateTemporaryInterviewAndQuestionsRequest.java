package org.hoongoin.interviewbank.interview.controller.request;

import org.hoongoin.interviewbank.interview.enums.CareerYear;
import org.hoongoin.interviewbank.interview.enums.InterviewPeriod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateTemporaryInterviewAndQuestionsRequest {

	private String title;
	private long jobCategoryId;
	private InterviewPeriod interviewPeriod;
	private CareerYear careerYear;
	private QuestionsRequest questionsRequest;
}
