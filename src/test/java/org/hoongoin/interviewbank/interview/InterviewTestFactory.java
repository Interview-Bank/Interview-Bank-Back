package org.hoongoin.interviewbank.interview;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.interview.application.entity.Interview;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewAndQuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.request.QuestionsRequest;
import org.hoongoin.interviewbank.interview.enums.CareerYear;
import org.hoongoin.interviewbank.interview.enums.InterviewPeriod;
import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.QuestionEntity;

public class InterviewTestFactory {

	public static CreateInterviewAndQuestionsRequest createCreateInterviewAndQuestionsRequest(int size) {
		String title = "title";

		List<QuestionsRequest.Question> questions = IntStream.range(1, size + 1)
			.mapToObj(i -> "content" + i)
			.map(QuestionsRequest.Question::new)
			.collect(Collectors.toList());

		QuestionsRequest questionsRequest = new QuestionsRequest(questions);

		return getCreateInterviewAndQuestionsRequest(title, questionsRequest);
	}

	public static InterviewEntity createInterviewEntity(AccountEntity accountEntity,
		JobCategoryEntity jobCategoryEntity) {
		return InterviewEntity.builder()
			.title("title")
			.accountEntity(accountEntity)
			.jobCategoryEntity(jobCategoryEntity)
			.build();
	}

	public static QuestionEntity createQuestionEntity(String content, InterviewEntity interviewEntity) {
		return QuestionEntity.builder()
			.content(content)
			.interviewEntity(interviewEntity)
			.build();
	}

	public static JobCategoryEntity createJobCategoryEntity() {
		return JobCategoryEntity.builder()
			.name("job category")
			.build();
	}

	public static Interview createInterview(long accountId) {
		return Interview
			.builder()
			.accountId(accountId)
			.interviewId(3L)
			.jobCategoryId(1L)
			.title("title")
			.build();
	}

	private static CreateInterviewAndQuestionsRequest getCreateInterviewAndQuestionsRequest(String title,
		QuestionsRequest questionsRequest) {
		return new CreateInterviewAndQuestionsRequest(
			title, 1, InterviewPeriod.EXPECTED_INTERVIEW, CareerYear.FOUR_YEAR, null, questionsRequest);
	}
}
