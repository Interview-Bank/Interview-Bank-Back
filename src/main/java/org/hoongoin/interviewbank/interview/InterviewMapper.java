package org.hoongoin.interviewbank.interview;

import org.hoongoin.interviewbank.account.entity.AccountEntity;
import org.hoongoin.interviewbank.account.service.domain.Account;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.response.FindInterviewResponse;
import org.hoongoin.interviewbank.interview.controller.response.UpdateInterviewResponse;
import org.hoongoin.interviewbank.interview.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.service.domain.Interview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InterviewMapper {

	@Mapping(target = "id", source = "interview.interviewId")
	InterviewEntity interviewToInterviewEntity(Interview interview, AccountEntity accountEntity);

	@Mapping(target = "interviewId", source = "interviewEntity.id")
	@Mapping(target = "createdAt", source = "interviewEntity.createdAt")
	@Mapping(target = "updatedAt", source = "interviewEntity.updatedAt")
	@Mapping(target = "deletedAt", source = "interviewEntity.deletedAt")
	@Mapping(target = "deletedFlag", source = "interviewEntity.deletedFlag")
	Interview interviewEntityToInterview(InterviewEntity interviewEntity, Account account);

	@Mapping(target = "createInterviewRequest.accountId", ignore = true)
	@Mapping(target = "accountEntity", source = "accountEntity")
	InterviewEntity createInterviewRequestToInterviewEntity(CreateInterviewRequest createInterviewRequest, AccountEntity accountEntity);

	UpdateInterviewResponse interviewToUpdateInterviewResponse(Interview interview);

	FindInterviewResponse interviewToFindInterviewResponse(Interview interview);
}
