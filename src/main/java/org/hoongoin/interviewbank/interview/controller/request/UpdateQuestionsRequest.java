package org.hoongoin.interviewbank.interview.controller.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateQuestionsRequest {

	public UpdateQuestionsRequest(List<Question> questions) {
		this.questions = questions;
	}

	private List<Question> questions;

	@Getter
	@Setter
	public static class Question {

		public Question(String content, Long questionId) {
			this.content = content;
			this.questionId = questionId;
		}

		private String content;
		private Long questionId;
	}
}

