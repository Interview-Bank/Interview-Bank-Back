package org.hoongoin.interviewbank.interview;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.account.entity.AccountEntity;
import org.hoongoin.interviewbank.account.service.domain.Account;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewAndQuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.request.UpdateInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.response.FindInterviewPageResponse;
import org.hoongoin.interviewbank.interview.controller.response.FindInterviewResponse;
import org.hoongoin.interviewbank.interview.controller.response.UpdateInterviewResponse;
import org.hoongoin.interviewbank.interview.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.entity.QuestionEntity;
import org.hoongoin.interviewbank.interview.service.domain.Interview;
import org.hoongoin.interviewbank.interview.service.domain.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InterviewMapper {

	@Mapping(target = "id", source = "interview.interviewId")
	InterviewEntity interviewToInterviewEntity(Interview interview, AccountEntity accountEntity);

	default Interview interviewEntityToInterview(InterviewEntity interviewEntity, Account account) {
		return new Interview(interviewEntity.getId(), interviewEntity.getTitle(), account.getAccountId(),
			interviewEntity.getCreatedAt(),
			interviewEntity.getUpdatedAt(), interviewEntity.getDeletedAt(), interviewEntity.getDeletedFlag(),
			account.getNickname());
	}

	default Question questionEntityToQuestion(QuestionEntity questionEntity) {
		return new Question(questionEntity.getId(), questionEntity.getInterviewEntity().getId(),
			questionEntity.getContent());
	}

	default FindInterviewResponse questionListAndInterviewToFindInterviewResponse(List<Question> questions,
		Interview interview) {
		List<FindInterviewResponse.Question> findInterviewResponseQuestions = new ArrayList<>();
		questions.forEach(question -> findInterviewResponseQuestions.add(
			new FindInterviewResponse.Question(question.getQuestionId(), question.getContent(), question.getCreatedAt(),
				question.getUpdatedAt(), question.getDeletedAt(), question.getDeletedFlag())));
		return new FindInterviewResponse(interview.getInterviewId(), interview.getTitle(), interview.getAccountId(),
			interview.getCreatedAt(), interview.getUpdatedAt(), interview.getDeletedAt(), interview.getDeletedFlag(),
			findInterviewResponseQuestions);
	}

	default FindInterviewPageResponse.Interview interviewAndNicknameToFindInterviewPageResponseInterview(
		Interview interview) {
		return new FindInterviewPageResponse.Interview(interview.getInterviewId(), interview.getNickname(),
			interview.getCreatedAt(), interview.getTitle());
	}

	default List<Question> updateInterviewRequestToQuestions(UpdateInterviewRequest updateInterviewRequest, long interviewId) {
		List<Question> questions = new ArrayList<>();
		updateInterviewRequest.getQuestions()
			.forEach(question -> questions.add(new Question(question.getQuestionId(), interviewId, question.getContent())));
		return questions;
	}

	default UpdateInterviewResponse questionsAndTitleToUpdateInterviewResponse(List<Question> questions, String title) {
		List<UpdateInterviewResponse.Question> updateInterviewResponseQuestions = new ArrayList<>();

		questions.forEach(question -> updateInterviewResponseQuestions.add(
			new UpdateInterviewResponse.Question(question.getQuestionId(), question.getInterviewId(),
				question.getContent(), question.getUpdatedAt())));

		return new UpdateInterviewResponse(title, updateInterviewResponseQuestions);
	}

	default List<Question> createInterviewAndQuestionsRequestToQuestions(
		CreateInterviewAndQuestionsRequest createInterviewAndQuestionsRequest, long interviewId) {
		List<Question> questions = new ArrayList<>();

		createInterviewAndQuestionsRequest.getQuestionsRequest()
			.getQuestions()
			.forEach(question -> questions.add(new Question(interviewId, question.getContent())));

		return questions;
	}
}