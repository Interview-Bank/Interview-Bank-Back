package org.hoongoin.interviewbank.interview.service.domain;

import java.time.LocalDateTime;

import org.hoongoin.interviewbank.exception.IbValidationException;

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
		this.setQuestionId(questionId);
		this.setInterviewId(interviewId);
		this.setContent(content);
		this.setCreatedAt(createdAt);
		this.setUpdatedAt(updatedAt);
		this.setDeletedAt(deletedAt);
		this.setDeletedFlag(deletedFlag);
	}

	private Long questionId;
	private Long interviewId;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deletedAt;
	private Boolean deletedFlag;

	public void setContent(String content) {
		if (validateContent(content)) {
			throw new IbValidationException("Question");
		}
		this.content = content;
	}

	private boolean validateContent(String content) {
		return content.getBytes().length < 1 || content.getBytes().length > 100000 || content.isEmpty();
	}
}
