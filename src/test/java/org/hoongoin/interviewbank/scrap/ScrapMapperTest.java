package org.hoongoin.interviewbank.scrap;

import static org.assertj.core.api.Assertions.*;

import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.scrap.application.entity.Scrap;
import org.hoongoin.interviewbank.scrap.application.entity.ScrapQuestion;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapEntity;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapQuestionEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class ScrapMapperTest {

	private static ScrapMapper scrapMapper;
	private static AccountEntity accountEntity;
	private static InterviewEntity interviewEntity;
	private static ScrapEntity scrapEntity;
	private static ScrapQuestionEntity scrapQuestionEntity;

	@BeforeAll
	public static void setUp() {
		scrapMapper = Mappers.getMapper(ScrapMapper.class);
		accountEntity = AccountEntity.builder()
			.id(12)
			.nickname("nickname")
			.email("test@test.com")
			.password("password")
			.build();
		interviewEntity = InterviewEntity.builder()
			.id(333)
			.title("title")
			.accountEntity(accountEntity)
			.build();
		scrapEntity = ScrapEntity.builder()
			.id(9)
			.accountEntity(accountEntity)
			.interviewEntity(interviewEntity)
			.title("scrap title")
			.build();
		scrapQuestionEntity = ScrapQuestionEntity.builder()
			.id(29)
			.scrapEntity(scrapEntity)
			.content("scrap question content")
			.build();
	}

	@Test
	public void scrapEntityToScrap_Success() {
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
	public void scrapQuestionEntityToScrapQuestion_Success() {
		//given

		//when
		ScrapQuestion scrapQuestion = scrapMapper.scrapQuestionEntityToScrapQuestion(scrapQuestionEntity);

		//then
		assertThat(scrapQuestion.getScrapQuestionId()).isEqualTo(scrapQuestionEntity.getId());
		assertThat(scrapQuestion.getScrapId()).isEqualTo(scrapEntity.getId());
		assertThat(scrapQuestion.getContent()).isEqualTo(scrapQuestionEntity.getContent());
	}
}
