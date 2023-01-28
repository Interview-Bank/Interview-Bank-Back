package org.hoongoin.interviewbank.interview.controller.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateInterviewResponse {

	private String title;
	private List<UpdateInterviewResponse.Question> questions;

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