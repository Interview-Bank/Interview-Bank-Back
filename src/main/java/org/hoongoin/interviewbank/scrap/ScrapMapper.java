package org.hoongoin.interviewbank.scrap;

import java.util.ArrayList;

import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.application.entity.Interview;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryEntity;
import org.hoongoin.interviewbank.scrap.controller.response.CreateScrapResponse;
import org.hoongoin.interviewbank.scrap.controller.response.ReadScrapDetailResponse;
import org.hoongoin.interviewbank.scrap.controller.response.UpdateScrapAnswerResponse;
import org.hoongoin.interviewbank.scrap.controller.response.UpdateScrapResponse;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapAnswerEntity;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapEntity;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapQuestionEntity;
import org.hoongoin.interviewbank.scrap.application.entity.Scrap;
import org.hoongoin.interviewbank.scrap.application.entity.ScrapAnswer;
import org.hoongoin.interviewbank.scrap.application.entity.ScrapQuestion;
import org.hoongoin.interviewbank.scrap.application.entity.ScrapQuestionWithScrapAnswers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScrapMapper {

	@Mapping(target = "jobCategoryId", source = "jobCategoryEntity.id")
	@Mapping(target = "scrapId", source = "id")
	@Mapping(target = "accountId", source = "accountEntity.id")
	@Mapping(target = "interviewId", source = "interviewEntity.id")
	Scrap scrapEntityToScrap(ScrapEntity scrapEntity);

	default ScrapEntity scrapToScrapEntity(Scrap scrap, Account account, Interview interview) {
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
		JobCategoryEntity jobCategoryEntity = JobCategoryEntity.builder()
			.id(interview.getJobCategoryId())
			.build();

		return ScrapEntity.builder()
			.accountEntity(accountEntity)
			.interviewEntity(interviewEntity)
			.jobCategoryEntity(jobCategoryEntity)
			.title(scrap.getTitle())
			.isPublic(scrap.getIsPublic())
			.view(scrap.getView())
			.build();
	}

	@Mapping(target = "scrapQuestionId", source = "id")
	@Mapping(target = "scrapId", source = "scrapEntity.id")
	ScrapQuestion scrapQuestionEntityToScrapQuestion(ScrapQuestionEntity scrapQuestionEntity);

	ReadScrapDetailResponse.ScrapResponse scrapToReadScrapDetailResponseOfScrapResponse(Scrap scrap);

	CreateScrapResponse.ScrapQuestionAndScrapAnswerResponse.ScrapQuestionResponse scrapQuestionToScrapQuestionResponse(ScrapQuestion scrapQuestion);

	UpdateScrapResponse scrapToUpdateScrapResponse(Scrap scrap);

	@Mapping(target = "scrapAnswerId", source = "id")
	@Mapping(target = "scrapQuestionId", source = "scrapQuestionEntity.id")
	ScrapAnswer scrapAnswerEntityToScrapAnswer(ScrapAnswerEntity scrapAnswerEntity);

	UpdateScrapAnswerResponse scrapAnswerToUpdateScrapAnswerResponse(ScrapAnswer scrapAnswer);

	default ScrapQuestionWithScrapAnswers scrapQuestionEntityWithScrapAnswerEntitiesToScrapQuestionWithScrapAnswers(
		ScrapQuestionEntity scrapQuestionEntity) {
		ScrapQuestionWithScrapAnswers scrapQuestionWithScrapAnswers = ScrapQuestionWithScrapAnswers.builder()
			.scrapQuestionId(scrapQuestionEntity.getId())
			.scrapId(scrapQuestionEntity.getScrapEntity().getId())
			.content(scrapQuestionEntity.getContent())
			.createdAt(scrapQuestionEntity.getCreatedAt())
			.updatedAt(scrapQuestionEntity.getUpdatedAt())
			.scrapAnswers(new ArrayList<>())
			.build();

		scrapQuestionEntity.getScrapAnswerEntities().forEach(
			scrapAnswerEntity -> {
				scrapQuestionWithScrapAnswers.addScrapAnswer(this.scrapAnswerEntityToScrapAnswer(scrapAnswerEntity));
			}
		);

		return scrapQuestionWithScrapAnswers;
	}

	@Mapping(target = "scrapAnswerResponseList", source = "scrapAnswers")
	ReadScrapDetailResponse.ScrapQuestionWithScrapAnswersResponse scrapQuestionWithScrapAnswersToScrapQuestionWithScrapAnswersResponse(
		ScrapQuestionWithScrapAnswers scrapQuestionWithScrapAnswers);

	CreateScrapResponse.ScrapQuestionAndScrapAnswerResponse.ScrapAnswerResponse scrapAnswerToScrapAnswerResponse(ScrapAnswer scrapAnswer);

	CreateScrapResponse.ScrapResponse scrapToCreateScrapResponseOfScrapResponse(Scrap scrap);

}
