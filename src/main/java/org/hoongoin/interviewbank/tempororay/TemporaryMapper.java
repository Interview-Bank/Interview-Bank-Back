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

	default TemporaryInterview createTemporaryInterviewAndQuestionRequestToTemporaryInterview(
		CreateTemporaryInterviewAndQuestionsRequest createTemporaryInterviewAndQuestionRequest, long accountId) {
		return TemporaryInterview.builder()
			.accountId(accountId)
			.title(createTemporaryInterviewAndQuestionRequest.getTitle())
			.jobCategoryId(createTemporaryInterviewAndQuestionRequest.getJobCategoryId())
			.interviewPeriod(createTemporaryInterviewAndQuestionRequest.getInterviewPeriod())
			.careerYear(createTemporaryInterviewAndQuestionRequest.getCareerYear())
			.temporaryInterviewId(createTemporaryInterviewAndQuestionRequest.getInterviewId())
			.build();
	}

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
			.jobCategoryId(temporaryInterviewEntity.getJobCategoryId())
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

	default List<Long> temporaryQuestionsToTemporaryQuestionIds(List<TemporaryQuestion> temporaryQuestions) {
		List<Long> temporaryQuestionIds = new ArrayList<>();
		temporaryQuestions.forEach(
			temporaryQuestion -> temporaryQuestionIds.add(temporaryQuestion.getTemporaryQuestionId()));
		return temporaryQuestionIds;
	}

	default List<TemporaryQuestionEntity> temporaryQuestionsToTemporaryQuestionEntities(
		List<TemporaryQuestion> temporaryQuestions,
		TemporaryInterviewEntity temporaryInterviewEntity) {
		List<TemporaryQuestionEntity> temporaryQuestionEntities = new ArrayList<>();

		for (TemporaryQuestion temporaryQuestion : temporaryQuestions) {
			temporaryQuestionEntities.add(TemporaryQuestionEntity.builder()
				.content(temporaryQuestion.getContent())
				.temporaryInterviewEntity(temporaryInterviewEntity)
				.build());
		}

		return temporaryQuestionEntities;
	}

	default List<Long> temporaryQuestionEntitiesToIds(List<TemporaryQuestionEntity> temporaryQuestionEntities) {
		List<Long> temporaryInterviewIds = new ArrayList<>();
		temporaryQuestionEntities.forEach(
			temporaryQuestionEntity -> temporaryInterviewIds.add(temporaryQuestionEntity.getId()));
		return temporaryInterviewIds;
	}
}
