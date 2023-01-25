package org.hoongoin.interviewbank.interview.controller.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionsRequest {

	public QuestionsRequest(List<Question> questions) {
		this.questions = questions;
	}

	private List<Question> questions;

	@Getter
	public static class Question {

		public Question(String content) {
			this.content = content;
		}

		private String content;
	}
}
