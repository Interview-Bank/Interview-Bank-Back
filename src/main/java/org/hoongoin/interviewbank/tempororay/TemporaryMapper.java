package org.hoongoin.interviewbank.tempororay;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.tempororay.application.entity.TemporaryInterview;
import org.hoongoin.interviewbank.tempororay.application.entity.TemporaryQuestion;
import org.hoongoin.interviewbank.tempororay.controller.request.CreateTemporaryInterviewAndQuestionsRequest;
import org.hoongoin.interviewbank.tempororay.controller.response.FindTemporaryInterviewByIdResponse;
import org.hoongoin.interviewbank.tempororay.infrastructure.entity.TemporaryInterviewEntity;
import org.hoongoin.interviewbank.tempororay.infrastructure.entity.TemporaryQuestionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TemporaryMapper {
	TemporaryInterview createTemporaryInterviewAndQuestionRequestToTemporaryInterview(
		CreateTemporaryInterviewAndQuestionsRequest createTemporaryInterviewAndQuestionRequest, long accountId);

	default List<TemporaryQuestion> createTemporaryInterviewAndQuestionsToTemporaryQuestions(
		CreateTemporaryInterviewAndQuestionsRequest createTemporaryInterviewAndQuestionsRequest,
		long temporaryInterviewId) {
		List<TemporaryQuestion> temporaryQuestions = new ArrayList<>();

		createTemporaryInterviewAndQuestionsRequest.getQuestionsRequest()
			.getQuestions()
			.forEach(question -> temporaryQuestions.add(
				TemporaryQuestion.builder()
					.temporaryQuestionId(temporaryInterviewId)
					.content(question.getContent())
					.build()));

		return temporaryQuestions;
	}

	default TemporaryInterview temporaryInterviewEntityToTemporaryInterview(
		TemporaryInterviewEntity temporaryInterviewEntity) {
		return TemporaryInterview.builder()
			.temporaryInterviewId(temporaryInterviewEntity.getId())
			.title(temporaryInterviewEntity.getTitle())
			.accountId(temporaryInterviewEntity.getAccountEntity().getId())
			.createdAt(temporaryInterviewEntity.getCreatedAt())
			.updatedAt(temporaryInterviewEntity.getUpdatedAt())
			.jobCategoryId(temporaryInterviewEntity.getJobCategoryEntity().getId())
			.careerYear(temporaryInterviewEntity.getCareerYear())
			.interviewPeriod(temporaryInterviewEntity.getInterviewPeriod())
			.build();
	}

	default TemporaryQuestion temporaryQuestionEntityToTemporaryQuestion(
		TemporaryQuestionEntity temporaryQuestionEntity) {
		return TemporaryQuestion.builder()
			.temporaryQuestionId(temporaryQuestionEntity.getId())
			.temporaryInterviewId(temporaryQuestionEntity.getTemporaryInterviewEntity().getId())
			.content(temporaryQuestionEntity.getContent())
			.createdAt(temporaryQuestionEntity.getCreatedAt())
			.updatedAt(temporaryQuestionEntity.getUpdatedAt())
			.build();
	}

	default List<TemporaryQuestion> temporaryQuestionEntitiesToTemporaryQuestions(
		List<TemporaryQuestionEntity> temporaryQuestionEntities) {
		List<TemporaryQuestion> questions = new ArrayList<>();
		for (TemporaryQuestionEntity temporaryQuestionEntity : temporaryQuestionEntities) {
			questions.add(temporaryQuestionEntityToTemporaryQuestion(temporaryQuestionEntity));
		}
		return questions;
	}

	default List<FindTemporaryInterviewByIdResponse.TemporaryQuestion> temporaryQuestionsToTemporaryQuestionResponse(
		List<TemporaryQuestion> temporaryQuestions) {
		List<FindTemporaryInterviewByIdResponse.TemporaryQuestion> temporaryQuestionsResponse = new ArrayList<>();
		temporaryQuestions.forEach(temporaryQuestion -> temporaryQuestionsResponse.add(
			new FindTemporaryInterviewByIdResponse.TemporaryQuestion(temporaryQuestion.getTemporaryQuestionId(),
				temporaryQuestion.getContent(), temporaryQuestion.getCreatedAt(), temporaryQuestion.getUpdatedAt())));
		return temporaryQuestionsResponse;
	}
}
