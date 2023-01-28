package org.hoongoin.interviewbank.interview.controller.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FindInterviewResponse {

	private Long interviewId;
	private String title;
	private Long accountId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deletedAt;
	private Boolean deletedFlag;
	private List<Question> questions;

	@Getter
	public static class Question {

		public Question(Long questionId, String content, LocalDateTime createdAt, LocalDateTime updatedAt,
			LocalDateTime deletedAt, Boolean deletedFlag) {
			this.questionId = questionId;
			this.content = content;
			this.createdAt = createdAt;
			this.updatedAt = updatedAt;
			this.deletedAt = deletedAt;
			this.deletedFlag = deletedFlag;
		}

		private Long questionId;
		private String content;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
		private LocalDateTime deletedAt;
		private Boolean deletedFlag;
	}
}