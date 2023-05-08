package org.hoongoin.interviewbank.interview.controller.response;

import java.time.LocalDateTime;
import java.util.List;

import org.hoongoin.interviewbank.interview.enums.CareerYear;
import org.hoongoin.interviewbank.interview.enums.InterviewPeriod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FindInterviewResponse {

	private Long interviewId;
	private String title;
	private Long accountId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private List<Question> questions;
	private InterviewPeriod interviewPeriod;
	private CareerYear careerYear;
	private JobCategoryResponse jobCategory;

	@Getter
	@AllArgsConstructor
	public static class Question {

		private Long questionId;
		private String content;
		private String gptAnswer;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
		private LocalDateTime deletedAt;
		private Boolean deletedFlag;
	}
}
