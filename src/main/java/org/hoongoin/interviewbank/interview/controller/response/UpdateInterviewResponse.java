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
public class UpdateInterviewResponse {

	private String title;
	private List<UpdateInterviewResponse.Question> questions;
	private String primaryJobCategory;
	private String secondaryJobCategory;
	private InterviewPeriod interviewPeriod;
	private CareerYear careerYear;

	@Getter
	public static class Question {

		public Question(long questionId, String content, LocalDateTime updatedAt) {
			this.questionId = questionId;
			this.content = content;
			this.updatedAt = updatedAt;
		}

		private long questionId;
		private String content;
		private LocalDateTime updatedAt;
	}
}
