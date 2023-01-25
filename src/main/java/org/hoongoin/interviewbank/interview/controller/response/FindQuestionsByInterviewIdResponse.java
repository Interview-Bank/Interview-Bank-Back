package org.hoongoin.interviewbank.interview.controller.response;

import java.util.List;

import org.hoongoin.interviewbank.interview.service.domain.Question;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindQuestionsByInterviewIdResponse {

	public FindQuestionsByInterviewIdResponse(
		List<Question> questions) {
		this.questions = questions;
	}

	private List<Question> questions;
}
