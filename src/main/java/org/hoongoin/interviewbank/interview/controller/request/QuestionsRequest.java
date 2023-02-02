package org.hoongoin.interviewbank.interview.controller.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionsRequest {

	private List<Question> questions;

	@Getter
	@Setter
	@NoArgsConstructor
	public static class Question {

		public Question(String content) {
			this.content = content;
		}

		private String content;
	}
}