package org.hoongoin.interviewbank.interview.controller.response;

import java.time.LocalDateTime;

import org.hoongoin.interviewbank.account.service.domain.Account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindInterviewResponse {

	private long interviewId;
	private String title;
	private Account account;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deletedAt;
	private boolean deletedFlag;
}
