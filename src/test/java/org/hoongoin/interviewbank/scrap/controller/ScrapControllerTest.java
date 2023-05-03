package org.hoongoin.interviewbank.scrap.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.hoongoin.interviewbank.account.AccountTestFactory;
import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.config.IbSpringBootTest;
import org.hoongoin.interviewbank.interview.InterviewTestFactory;
import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.QuestionEntity;
import org.hoongoin.interviewbank.interview.infrastructure.repository.InterviewRepository;
import org.hoongoin.interviewbank.interview.infrastructure.repository.JobCategoryRepository;
import org.hoongoin.interviewbank.interview.infrastructure.repository.QuestionRepository;
import org.hoongoin.interviewbank.scrap.ScrapTestFactory;
import org.hoongoin.interviewbank.scrap.controller.request.CreateScrapRequest;
import org.hoongoin.interviewbank.scrap.controller.request.UpdateScrapRequest;
import org.hoongoin.interviewbank.scrap.controller.response.CreateScrapResponse;
import org.hoongoin.interviewbank.scrap.controller.response.UpdateScrapResponse;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapEntity;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapQuestionEntity;
import org.hoongoin.interviewbank.scrap.infrastructure.repository.ScrapQuestionRepository;
import org.hoongoin.interviewbank.scrap.infrastructure.repository.ScrapRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@IbSpringBootTest
@Transactional
@Sql(scripts = "classpath:/account-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ScrapControllerTest {

	@Autowired
	private ScrapController scrapController;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private InterviewRepository interviewRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private ScrapRepository scrapRepository;

	@Autowired
	private ScrapQuestionRepository scrapQuestionRepository;

	@Autowired
	private JobCategoryRepository jobCategoryRepository;

	@Test
	void createScrap_Success() {
		//given
		AccountEntity interviewWriterAccountEntity = AccountTestFactory.createAccountEntity();
		accountRepository.saveAndFlush(interviewWriterAccountEntity);

		JobCategoryEntity jobCategoryEntity = jobCategoryRepository.saveAndFlush(InterviewTestFactory.createJobCategoryEntity());

		InterviewEntity interviewEntity = InterviewTestFactory.createInterviewEntity(interviewWriterAccountEntity,
			jobCategoryEntity);
		interviewRepository.saveAndFlush(interviewEntity);

		QuestionEntity questionEntity1 = InterviewTestFactory.createQuestionEntity("content 1", interviewEntity);
		QuestionEntity questionEntity2 = InterviewTestFactory.createQuestionEntity("content 2", interviewEntity);
		questionRepository.saveAndFlush(questionEntity1);
		questionRepository.saveAndFlush(questionEntity2);

		CreateScrapRequest createScrapRequest = new CreateScrapRequest(interviewEntity.getId());

		//when
		ResponseEntity<CreateScrapResponse> createScrapResponse = scrapController.createScrap(createScrapRequest);

		//then
		assertThat(createScrapResponse.getBody().getOriginalInterview().getInterviewId()).isEqualTo(
			interviewEntity.getId());
		assertThat(createScrapResponse.getBody().getOriginalInterview().getTitle()).isEqualTo(
			interviewEntity.getTitle());

		assertThat(createScrapResponse.getBody().getScrap().getTitle()).isEqualTo(interviewEntity.getTitle());

		assertThat(createScrapResponse.getBody().getScrapQuestionAndScrapAnswerList().size()).isEqualTo(2);
		assertThat(createScrapResponse.getBody()
			.getScrapQuestionAndScrapAnswerList()
			.get(0)
			.getScrapQuestion()
			.getContent()).isEqualTo(
			questionEntity1.getContent());
		assertThat(createScrapResponse.getBody()
			.getScrapQuestionAndScrapAnswerList()
			.get(1)
			.getScrapQuestion()
			.getContent()).isEqualTo(
			questionEntity2.getContent());
	}

	@Test
	void updateScrap_Success() {
		//given
		AccountEntity interviewWriterAccountEntity = AccountTestFactory.createAccountEntity();
		accountRepository.saveAndFlush(interviewWriterAccountEntity);

		JobCategoryEntity jobCategoryEntity = jobCategoryRepository.saveAndFlush(InterviewTestFactory.createJobCategoryEntity());

		InterviewEntity interviewEntity = InterviewTestFactory.createInterviewEntity(interviewWriterAccountEntity,
			jobCategoryEntity);
		interviewRepository.saveAndFlush(interviewEntity);

		QuestionEntity questionEntity1 = InterviewTestFactory.createQuestionEntity("content 1", interviewEntity);
		QuestionEntity questionEntity2 = InterviewTestFactory.createQuestionEntity("content 2", interviewEntity);
		questionRepository.saveAndFlush(questionEntity1);
		questionRepository.saveAndFlush(questionEntity2);

		AccountEntity scrapWriterAccountEntity = accountRepository.findById(1L).get();

		ScrapEntity scrapEntity = ScrapTestFactory.createScrapEntity(interviewEntity, scrapWriterAccountEntity);
		scrapRepository.saveAndFlush(scrapEntity);

		ScrapQuestionEntity scrapQuestionEntity1 = ScrapTestFactory.createScrapQuestionEntity(scrapEntity,
			questionEntity1.getContent());
		ScrapQuestionEntity scrapQuestionEntity2 = ScrapTestFactory.createScrapQuestionEntity(scrapEntity,
			questionEntity2.getContent());
		scrapQuestionRepository.saveAndFlush(scrapQuestionEntity1);
		scrapQuestionRepository.saveAndFlush(scrapQuestionEntity2);

		UpdateScrapRequest updateScrapRequest = new UpdateScrapRequest("update title");

		//when
		ResponseEntity<UpdateScrapResponse> updateScrapResponse = scrapController.updateScrap(updateScrapRequest,
			scrapEntity.getId());

		//then
		assertThat(updateScrapResponse.getBody().getTitle()).isEqualTo(updateScrapRequest.getTitle());
	}
}
