package org.hoongoin.interviewbank.scrap.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateScrapRequest {

	private long interviewId;
	private Boolean isPublic;
}
