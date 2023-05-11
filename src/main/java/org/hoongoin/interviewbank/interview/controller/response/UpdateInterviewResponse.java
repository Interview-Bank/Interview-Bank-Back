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
public class UpdateInterviewResponse {

	private String title;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private List<UpdateInterviewResponse.Question> questions;
	private Long jobCategoryId;
	private InterviewPeriod interviewPeriod;
	private CareerYear careerYear;
	private JobCategoryResponse jobCategory;

	@AllArgsConstructor
	@Getter
	public static class Question {

		private long questionId;
		private String content;
		private LocalDateTime updatedAt;
		private String gptAnswer;
	}
}
