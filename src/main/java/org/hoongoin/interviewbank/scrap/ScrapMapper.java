package org.hoongoin.interviewbank.scrap;

import org.hoongoin.interviewbank.account.entity.AccountEntity;
import org.hoongoin.interviewbank.account.service.domain.Account;
import org.hoongoin.interviewbank.interview.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.service.domain.Interview;
import org.hoongoin.interviewbank.scrap.controller.response.ScrapQuestionResponse;
import org.hoongoin.interviewbank.scrap.controller.response.ScrapResponse;
import org.hoongoin.interviewbank.scrap.entity.ScrapEntity;
import org.hoongoin.interviewbank.scrap.entity.ScrapQuestionEntity;
import org.hoongoin.interviewbank.scrap.service.domain.Scrap;
import org.hoongoin.interviewbank.scrap.service.domain.ScrapQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScrapMapper {

	@Mapping(target = "scrapId", source = "id")
	@Mapping(target = "accountId", source = "accountEntity.id")
	@Mapping(target = "interviewId", source = "interviewEntity.id")
	Scrap scrapEntityToScrap(ScrapEntity scrapEntity);

	default ScrapEntity scrapToScrapEntity(Scrap scrap, Account account, Interview interview){
		AccountEntity accountEntity = AccountEntity.builder()
			.id(account.getAccountId())
			.nickname(account.getNickname())
			.email(account.getPassword())
			.password(account.getPassword())
			.build();
		InterviewEntity interviewEntity = InterviewEntity.builder()
			.id(interview.getInterviewId())
			.title(interview.getTitle())
			.accountEntity(accountEntity)
			.build();

		return ScrapEntity.builder()
			.accountEntity(accountEntity)
			.interviewEntity(interviewEntity)
			.title(scrap.getTitle())
			.build();
	}


	@Mapping(target = "scrapQuestionId", source = "id")
	@Mapping(target = "scrapId", source = "scrapEntity.id")
	ScrapQuestion scrapQuestionEntityToScrapQuestion(ScrapQuestionEntity scrapQuestionEntity);

	ScrapResponse scrapToScrapResponse(Scrap scrap);

	ScrapQuestionResponse scrapQuestionToScrapQuestionResponse(ScrapQuestion scrapQuestion);

}
