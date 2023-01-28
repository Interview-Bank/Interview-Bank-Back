package org.hoongoin.interviewbank.interview;

import org.hoongoin.interviewbank.account.entity.AccountEntity;
import org.hoongoin.interviewbank.account.service.domain.Account;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewRequest;
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
		return new Interview(interviewEntity.getId(), interviewEntity.getTitle(), account.getAccountId(), interviewEntity.getCreatedAt(),
			interviewEntity.getUpdatedAt(), interviewEntity.getDeletedAt(), interviewEntity.getDeletedFlag());
	}

	@Mapping(target = "createInterviewRequest.accountId", ignore = true)
	@Mapping(target = "accountEntity", source = "accountEntity")
	InterviewEntity createInterviewRequestToInterviewEntity(CreateInterviewRequest createInterviewRequest,
		AccountEntity accountEntity);

	UpdateInterviewResponse interviewToUpdateInterviewResponse(Interview interview);

	FindInterviewResponse interviewToFindInterviewResponse(Interview interview);

	default Question questionEntityToQuestion(QuestionEntity questionEntity) {
		return new Question(questionEntity.getId(), questionEntity.getInterviewEntity().getId(),
			questionEntity.getContent());
	}
}
