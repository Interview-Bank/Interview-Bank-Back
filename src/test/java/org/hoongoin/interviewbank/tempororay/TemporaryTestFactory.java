package org.hoongoin.interviewbank.tempororay;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.interview.controller.request.QuestionsRequest;
import org.hoongoin.interviewbank.interview.enums.CareerYear;
import org.hoongoin.interviewbank.interview.enums.InterviewPeriod;
import org.hoongoin.interviewbank.tempororay.controller.request.CreateTemporaryInterviewAndQuestionsRequest;

public class TemporaryTestFactory {

	public static CreateTemporaryInterviewAndQuestionsRequest createTemporaryInterviewByTitleAndInterviewId(
		String title, Long interviewId) {
		Long jobCategoryId = 1L;
		InterviewPeriod interviewPeriod = InterviewPeriod.EXPECTED_INTERVIEW;
		CareerYear careerYear = CareerYear.FOUR_YEAR;
		QuestionsRequest questionsRequest = new QuestionsRequest(createDefaultQuestions());
		return new CreateTemporaryInterviewAndQuestionsRequest(title, jobCategoryId, interviewPeriod, careerYear,
			questionsRequest, interviewId);
	}

	private static List<QuestionsRequest.Question> createDefaultQuestions() {
		List<QuestionsRequest.Question> questions = new ArrayList<>();

		questions.add(new QuestionsRequest.Question("Hello"));
		questions.add(new QuestionsRequest.Question("World"));

		return questions;
	}
}
