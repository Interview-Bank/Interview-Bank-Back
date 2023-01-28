package org.hoongoin.interviewbank.scrap.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class OriginalInterviewResponse {

	private long interviewId;
	private String interviewTitle;
	private String interviewWriterAccountNickname;
}
