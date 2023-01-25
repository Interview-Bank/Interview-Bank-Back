package org.hoongoin.interviewbank.interview.controller.request;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateInterviewAndQuestionsResponse {

	public CreateInterviewAndQuestionsResponse(String title, Long interviewId,
		List<String> questionContents, List<Long> questionIds, LocalDateTime interviewCreatedAt) {
		this.title = title;
		this.interviewId = interviewId;
		this.questionContents = questionContents;
		this.questionIds = questionIds;
		this.interviewCreatedAt = interviewCreatedAt;
	}

	private String title;
	private Long interviewId;
	private List<String> questionContents;
	private List<Long> questionIds;
	private LocalDateTime interviewCreatedAt;
}
