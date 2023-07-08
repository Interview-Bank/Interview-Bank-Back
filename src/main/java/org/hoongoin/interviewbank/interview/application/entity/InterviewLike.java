package org.hoongoin.interviewbank.interview.application.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InterviewLike {

	@Builder
	public InterviewLike(Long interviewLikeId, Long accountId, Long interviewId, Boolean like){
		this.setInterviewLikeId(interviewLikeId);
		this.setAccountId(accountId);
		this.setInterviewId(interviewId);
		this.setLike(like);
	}

	private Long interviewLikeId;
	private Long accountId;
	private Long interviewId;
	private Boolean like;
}
