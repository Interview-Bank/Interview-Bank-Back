package org.hoongoin.interviewbank.interview.controller.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateTemporaryInterviewAndQuestionResponse {

	private long temporaryInterviewId;
	private List<Long> temporaryQuestionIds;
}
