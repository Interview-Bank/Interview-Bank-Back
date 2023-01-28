package org.hoongoin.interviewbank.interview.controller.request;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateQuestionsRequest {

	private List<Question> questions;

	@Getter
	@Setter
	public static class Question {

		public Question(long questionId, long interviewId, String content, LocalDateTime updatedAt) {
			this.questionId = questionId;
			this.interviewId = interviewId;
			this.content = content;
			this.updatedAt = updatedAt;
		}

		private long questionId;
		private long interviewId;
		private String content;
		private LocalDateTime updatedAt;
	}
}