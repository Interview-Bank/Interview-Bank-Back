package org.hoongoin.interviewbank.scrap;

import static org.assertj.core.api.Assertions.*;

import org.hoongoin.interviewbank.account.AccountTestFactory;
import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.interview.InterviewTestFactory;
import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryEntity;
import org.hoongoin.interviewbank.scrap.application.entity.Scrap;
import org.hoongoin.interviewbank.scrap.application.entity.ScrapQuestion;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapEntity;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapQuestionEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.transaction.annotation.Transactional;

class ScrapMapperTest {

	private static ScrapMapper scrapMapper;
	private static AccountEntity accountEntity;
	private static InterviewEntity interviewEntity;
	private static ScrapEntity scrapEntity;
	private static ScrapQuestionEntity scrapQuestionEntity;
	private static JobCategoryEntity jobCategoryEntity;

	@BeforeAll
	public static void setUp() {
		jobCategoryEntity = InterviewTestFactory.createJobCategoryEntity();
		scrapMapper = Mappers.getMapper(ScrapMapper.class);
		accountEntity = AccountTestFactory.createAccountEntity();
		interviewEntity = InterviewTestFactory.createInterviewEntity(accountEntity, jobCategoryEntity);
		scrapEntity = ScrapTestFactory.createScrapEntity(interviewEntity, accountEntity, jobCategoryEntity);
		scrapQuestionEntity = ScrapTestFactory.createScrapQuestionEntity(scrapEntity, "content");
	}

	@Test
	void scrapEntityToScrap_Success() {
		//given

		//when
		Scrap scrap = scrapMapper.scrapEntityToScrap(scrapEntity);

		//then
		assertThat(scrap.getScrapId()).isEqualTo(scrapEntity.getId());
		assertThat(scrap.getAccountId()).isEqualTo(accountEntity.getId());
		assertThat(scrap.getInterviewId()).isEqualTo(interviewEntity.getId());
		assertThat(scrap.getTitle()).isEqualTo(scrapEntity.getTitle());
	}

	@Test
	void scrapQuestionEntityToScrapQuestion_Success() {
		//given

		//when
		ScrapQuestion scrapQuestion = scrapMapper.scrapQuestionEntityToScrapQuestion(scrapQuestionEntity);

		//then
		assertThat(scrapQuestion.getScrapQuestionId()).isEqualTo(scrapQuestionEntity.getId());
		assertThat(scrapQuestion.getScrapId()).isEqualTo(scrapEntity.getId());
		assertThat(scrapQuestion.getContent()).isEqualTo(scrapQuestionEntity.getContent());
	}
}
