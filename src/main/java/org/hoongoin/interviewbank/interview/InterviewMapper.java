package org.hoongoin.interviewbank.interview;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewAndQuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.request.UpdateInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.response.FindInterviewPageResponse;
import org.hoongoin.interviewbank.interview.controller.response.FindInterviewResponse;
import org.hoongoin.interviewbank.interview.controller.response.UpdateInterviewResponse;
import org.hoongoin.interviewbank.interview.enums.CareerYear;
import org.hoongoin.interviewbank.interview.enums.InterviewPeriod;
import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.QuestionEntity;
import org.hoongoin.interviewbank.interview.application.entity.Interview;
import org.hoongoin.interviewbank.interview.application.entity.Question;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InterviewMapper {

	default Interview interviewEntityToInterview(InterviewEntity interviewEntity, Account account) {
		return new Interview(interviewEntity.getId(), interviewEntity.getTitle(), account.getAccountId(),
			interviewEntity.getCreatedAt(), interviewEntity.getUpdatedAt(), interviewEntity.getDeletedAt(),
			interviewEntity.getDeletedFlag(), null, null, interviewEntity.getInterviewPeriod(),
			interviewEntity.getCareerYear());
	}

	default Question questionEntityToQuestion(QuestionEntity questionEntity) {
		return Question.builder()
			.questionId(questionEntity.getId())
			.interviewId(questionEntity.getInterviewEntity().getId())
			.content(questionEntity.getContent())
			.build();
	}

	default FindInterviewResponse questionListAndInterviewToFindInterviewResponse(List<Question> questions,
		Interview interview) {
		List<FindInterviewResponse.Question> findInterviewResponseQuestions = new ArrayList<>();
		questions.forEach(question -> findInterviewResponseQuestions.add(
			new FindInterviewResponse.Question(question.getQuestionId(), question.getContent(), question.getCreatedAt(),
				question.getUpdatedAt(), question.getDeletedAt(), question.getDeletedFlag())));
		return new FindInterviewResponse(interview.getInterviewId(), interview.getTitle(), interview.getAccountId(),
			interview.getCreatedAt(), interview.getUpdatedAt(), interview.getDeletedAt(), interview.getDeletedFlag(),
			findInterviewResponseQuestions, interview.getInterviewPeriod(), interview.getCareerYear());
	}

	default FindInterviewPageResponse.Interview interviewAndNicknameToFindInterviewPageResponseInterview(
		Interview interview, Account account) {
		return new FindInterviewPageResponse.Interview(interview.getInterviewId(), account.getNickname(),
			interview.getCreatedAt(), interview.getTitle(), interview.getInterviewPeriod(), interview.getCareerYear());
	}

	default List<Question> updateInterviewRequestToQuestions(UpdateInterviewRequest updateInterviewRequest,
		long interviewId) {
		List<Question> questions = new ArrayList<>();
		updateInterviewRequest.getQuestions()
			.forEach(
				question -> questions.add(Question.builder()
					.questionId(question.getQuestionId())
					.interviewId(interviewId)
					.content(question.getContent())
					.build())
			);
		return questions;
	}

	default UpdateInterviewResponse questionsAndTitleToUpdateInterviewResponse(List<Question> questions, String title,
		String primaryJobCategory, String secondaryJobCategory, InterviewPeriod interviewPeriod,
		CareerYear careerYear) {
		List<UpdateInterviewResponse.Question> updateInterviewResponseQuestions = new ArrayList<>();

		questions.forEach(question -> updateInterviewResponseQuestions.add(
			new UpdateInterviewResponse.Question(question.getQuestionId(), question.getContent(),
				question.getUpdatedAt())));
		return new UpdateInterviewResponse(title, updateInterviewResponseQuestions, primaryJobCategory,
			secondaryJobCategory, interviewPeriod, careerYear);
	}

	default List<Question> createInterviewAndQuestionsRequestToQuestions(
		CreateInterviewAndQuestionsRequest createInterviewAndQuestionsRequest, long interviewId) {
		List<Question> questions = new ArrayList<>();

		createInterviewAndQuestionsRequest.getQuestionsRequest()
			.getQuestions()
			.forEach(question -> questions.add(
				Question.builder()
					.interviewId(interviewId)
					.content(question.getContent())
					.build())
			);

		return questions;
	}
}
