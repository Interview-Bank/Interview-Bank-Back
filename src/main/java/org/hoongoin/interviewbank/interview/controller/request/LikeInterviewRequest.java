package org.hoongoin.interviewbank.interview.controller.request;

import lombok.Getter;

@Getter
public class LikeInterviewRequest {

	private boolean like;

	public boolean getLike() {
		return this.like;
	}
}
