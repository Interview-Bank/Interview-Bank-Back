package org.hoongoin.interviewbank.interview.application.entity;

import java.time.LocalDateTime;

import org.hoongoin.interviewbank.exception.IbValidationException;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@ToString
public class Question {

	@Builder
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
			log.info("Question Validation Failed");
			throw new IbValidationException("Question Validation Failed");
		}
		this.content = content;
	}

	private boolean validateContent(String content) {
		return content.getBytes().length < 1 || content.getBytes().length > 100000 || content.isEmpty();
	}
}
