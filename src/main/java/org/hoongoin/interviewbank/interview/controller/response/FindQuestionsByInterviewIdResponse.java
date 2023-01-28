package org.hoongoin.interviewbank.interview.controller.response;

import java.util.List;

import org.hoongoin.interviewbank.interview.service.domain.Question;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FindQuestionsByInterviewIdResponse {

	private List<Question> questions;
}