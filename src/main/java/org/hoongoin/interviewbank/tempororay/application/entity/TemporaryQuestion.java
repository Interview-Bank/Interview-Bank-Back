package org.hoongoin.interviewbank.tempororay.application.entity;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@ToString
public class TemporaryQuestion {

	@Builder
	public TemporaryQuestion(Long temporaryQuestionId, long temporaryInterviewId, String content,
		LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.setTemporaryQuestionId(temporaryQuestionId);
		this.setTemporaryInterviewId(temporaryInterviewId);
		this.setContent(content);
		this.setCreatedAt(createdAt);
		this.setUpdatedAt(updatedAt);
	}

	private Long temporaryQuestionId;
	private Long temporaryInterviewId;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
