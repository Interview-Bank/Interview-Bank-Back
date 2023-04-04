package org.hoongoin.interviewbank.interview.controller.response;

import java.time.LocalDateTime;
import java.util.List;

import org.hoongoin.interviewbank.interview.enums.CareerYear;
import org.hoongoin.interviewbank.interview.enums.InterviewPeriod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateInterviewAndQuestionsResponse {

	private String title;
	private Long interviewId;
	private List<Question> questions;
	private LocalDateTime interviewCreatedAt;
	private InterviewPeriod interviewPeriod;
	private CareerYear careerYear;

	private JobCategoryResponse jobCategory;

	@Getter
	@Setter
	@AllArgsConstructor
	public static class Question {

		private String content;
		private long questionId;
	}
}
