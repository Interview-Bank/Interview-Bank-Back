package org.hoongoin.interviewbank.interview;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.interview.application.entity.JobCategory;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewAndQuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.request.UpdateInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.response.FindInterviewPageResponse;
import org.hoongoin.interviewbank.interview.controller.response.FindInterviewResponse;
import org.hoongoin.interviewbank.interview.controller.response.JobCategoryResponse;
import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.QuestionEntity;
import org.hoongoin.interviewbank.interview.application.entity.Interview;
import org.hoongoin.interviewbank.interview.application.entity.Question;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InterviewMapper {

	default Interview interviewEntityToInterview(InterviewEntity interviewEntity, Long accountId) {
		if (interviewEntity.getJobCategoryEntity() == null) {
			return Interview.builder()
				.interviewId(interviewEntity.getId())
				.title(interviewEntity.getTitle())
				.accountId(accountId)
				.createdAt(interviewEntity.getCreatedAt())
				.updatedAt(interviewEntity.getUpdatedAt())
				.deletedAt(interviewEntity.getDeletedAt())
				.deletedFlag(interviewEntity.getDeletedFlag())
				.interviewPeriod(interviewEntity.getInterviewPeriod())
				.careerYear(interviewEntity.getCareerYear())
				.build();
		}
		return Interview.builder()
			.interviewId(interviewEntity.getId())
			.title(interviewEntity.getTitle())
			.accountId(accountId)
			.createdAt(interviewEntity.getCreatedAt())
			.updatedAt(interviewEntity.getUpdatedAt())
			.deletedAt(interviewEntity.getDeletedAt())
			.deletedFlag(interviewEntity.getDeletedFlag())
			.jobCategoryId(interviewEntity.getJobCategoryEntity().getId())
			.interviewPeriod(interviewEntity.getInterviewPeriod())
			.careerYear(interviewEntity.getCareerYear())
			.build();
	}

	default Question questionEntityToQuestion(QuestionEntity questionEntity) {
		return Question.builder()
			.questionId(questionEntity.getId())
			.interviewId(questionEntity.getInterviewEntity().getId())
			.content(questionEntity.getContent())
			.gptAnswer(questionEntity.getGptAnswer())
			.createdAt(questionEntity.getCreatedAt())
			.updatedAt(questionEntity.getUpdatedAt())
			.deletedAt(questionEntity.getDeletedAt())
			.deletedFlag(questionEntity.getDeletedFlag())
			.build();
	}

	default FindInterviewPageResponse.Interview interviewAndNicknameToFindInterviewPageResponseInterview(
		Interview interview, Account account, JobCategoryResponse jobCategoryResponse) {
		return new FindInterviewPageResponse.Interview(interview.getInterviewId(), account.getNickname(),
			interview.getCreatedAt(), interview.getTitle(), interview.getInterviewPeriod(), interview.getCareerYear(),
			jobCategoryResponse);
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

	Interview createInterviewAndQuestionsRequestToInterview(CreateInterviewAndQuestionsRequest createInterviewAndQuestionsRequest, long accountId);

	JobCategoryResponse jobCategoryToJobCategoryRespnose(JobCategory jobCategory);

	Interview updateInterviewRequestToInterview(UpdateInterviewRequest updateInterviewRequest, long interviewId, long accountId);
}
