package org.hoongoin.interviewbank.interview.controller.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateInterviewRequest {

	private List<UpdateInterviewRequest.Question> questions;
	private String title;

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
