package org.hoongoin.interviewbank.tempororay.controller.response;

import java.time.LocalDateTime;
import java.util.List;

import org.hoongoin.interviewbank.interview.controller.response.JobCategoryResponse;
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
public class FindTemporaryInterviewByIdResponse {
	private Long temporaryInterviewId;
	private String title;
	private Long accountId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private List<TemporaryQuestion> temporaryQuestions;
	private InterviewPeriod interviewPeriod;
	private CareerYear careerYear;
	private JobCategoryResponse jobCategoryResponse;

	@Getter
	@AllArgsConstructor
	public static class TemporaryQuestion {
		private Long temporaryQuestionId;
		private String content;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
	}
}
