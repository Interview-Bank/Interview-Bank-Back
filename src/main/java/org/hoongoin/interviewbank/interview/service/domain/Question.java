package org.hoongoin.interviewbank.interview.service.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Question {

	public Question(long interviewId, String content) {
		this(null, interviewId, content);
	}

	public Question(Long questionId, long interviewId, String content) {
		this(questionId, interviewId, content, null, null, null, null);
	}

	public Question(Long questionId, long interviewId, String content, LocalDateTime createdAt,
		LocalDateTime updatedAt, LocalDateTime deletedAt, Boolean deletedFlag) {
		this.questionId = questionId;
		this.interviewId = interviewId;
		this.content = content;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.deletedAt = deletedAt;
		this.deletedFlag = deletedFlag;
	}

	private Long questionId;
	private Long interviewId;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deletedAt;
	private Boolean deletedFlag;
}