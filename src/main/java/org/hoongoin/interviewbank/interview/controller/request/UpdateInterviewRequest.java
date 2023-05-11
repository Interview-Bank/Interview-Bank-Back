package org.hoongoin.interviewbank.interview.controller.request;

import java.util.List;

import org.hoongoin.interviewbank.interview.enums.CareerYear;
import org.hoongoin.interviewbank.interview.enums.InterviewPeriod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateInterviewRequest {

	private List<UpdateInterviewRequest.Question> questions;
	private String title;
	private Long jobCategoryId;
	private InterviewPeriod interviewPeriod;
	private CareerYear careerYear;

	@Getter
	@Setter
	@AllArgsConstructor
	public static class Question {

		private String content;
		private Long questionId;
	}
}
