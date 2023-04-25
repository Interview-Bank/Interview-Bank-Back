package org.hoongoin.interviewbank.interview;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewAndQuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.request.QuestionsRequest;
import org.hoongoin.interviewbank.interview.enums.CareerYear;
import org.hoongoin.interviewbank.interview.enums.InterviewPeriod;

public class InterviewTestFactory {

	public static CreateInterviewAndQuestionsRequest  createCreateInterviewAndQuestionsRequest(int size) {
		String title = "title";

		List<QuestionsRequest.Question> questions = IntStream.range(1, size+1)
			.mapToObj(i -> "content" + i)
			.map(QuestionsRequest.Question::new)
			.collect(Collectors.toList());

		QuestionsRequest questionsRequest = new QuestionsRequest(questions);

		return getCreateInterviewAndQuestionsRequest(title, questionsRequest);
	}

	private static CreateInterviewAndQuestionsRequest getCreateInterviewAndQuestionsRequest(String title,
		QuestionsRequest questionsRequest) {
		return new CreateInterviewAndQuestionsRequest(
			title, 1, InterviewPeriod.EXPECTED_INTERVIEW, CareerYear.FOUR_YEAR, questionsRequest);
	}
}
