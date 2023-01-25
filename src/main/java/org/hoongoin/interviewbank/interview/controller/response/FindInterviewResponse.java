package org.hoongoin.interviewbank.interview.controller.response;

import java.time.LocalDateTime;

import org.hoongoin.interviewbank.account.service.domain.Account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindInterviewResponse {

	public FindInterviewResponse(Long interviewId, String title,
		Long accountId, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt,
		Boolean deletedFlag) {
		this.interviewId = interviewId;
		this.title = title;
		this.accountId = accountId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.deletedAt = deletedAt;
		this.deletedFlag = deletedFlag;
	}

	private Long interviewId;
	private String title;
	private Long accountId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deletedAt;
	private Boolean deletedFlag;
}
