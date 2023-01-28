package org.hoongoin.interviewbank.interview.controller.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateInterviewAndQuestionsResponse {

	private String title;
	private Long interviewId;
	private List<Question> questions;
	private LocalDateTime interviewCreatedAt;

	@Getter
	@Setter
	@AllArgsConstructor
	public static class Question {

		private String content;
		private long questionId;
	}
}