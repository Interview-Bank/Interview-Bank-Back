package org.hoongoin.interviewbank.interview.controller.request;

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
	private List<String> questionContents;
	private List<Long> questionIds;
	private LocalDateTime interviewCreatedAt;
}