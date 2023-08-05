package org.hoongoin.interviewbank.tempororay.application;

import static org.assertj.core.api.Assertions.*;

import org.hoongoin.interviewbank.config.IbSpringBootTest;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.tempororay.TemporaryTestFactory;
import org.hoongoin.interviewbank.tempororay.controller.request.CreateTemporaryInterviewAndQuestionsRequest;
import org.hoongoin.interviewbank.tempororay.controller.response.CreateTemporaryInterviewAndQuestionResponse;
import org.hoongoin.interviewbank.tempororay.controller.response.DeleteTemporaryInterviewAndQuestionResponse;
import org.hoongoin.interviewbank.tempororay.controller.response.FindTemporaryInterviewByIdResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@IbSpringBootTest
@Sql(scripts = {
	"classpath:/account-data.sql",
	"classpath:/job-category-data.sql",
	"classpath:/temporary-interview-and-question-data.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TemporaryInterviewServiceTest {

	@Autowired
	private TemporaryInterviewService temporaryInterviewService;

	private static final String title = "Interview 1";
	private static final long accountId = 1L;

	@Test
	void createTemporaryInterviewAndQuestion_Success() {
		//given
		CreateTemporaryInterviewAndQuestionsRequest request = TemporaryTestFactory.createTemporaryInterviewByTitleAndInterviewId(
			title, null);

		//when
		CreateTemporaryInterviewAndQuestionResponse response = temporaryInterviewService.createTemporaryInterviewAndQuestion(
			request, accountId);

		//then
		assertThat(response.getTemporaryQuestionIds()).hasSize(2);
	}

	@Test
	void findTemporaryInterviewById_Success() {
		//given
		long temporaryInterviewId = 1L;

		//when
		FindTemporaryInterviewByIdResponse response = temporaryInterviewService.findTemporaryInterviewById(
			temporaryInterviewId);

		//then
		assertThat(response.getTitle()).isEqualTo(title);
		assertThat(response.getTemporaryQuestions()).hasSize(2);
	}

	@Test
	void deleteTemporaryInterviewAndQuestion_Success() {
		//given
		long temporaryInterviewId = 1L;

		//when
		DeleteTemporaryInterviewAndQuestionResponse response = temporaryInterviewService.deleteTemporaryInterviewAndQuestion(
			temporaryInterviewId, accountId);

		//then
		assertThatThrownBy(
			() -> temporaryInterviewService.findTemporaryInterviewById(response.getTemporaryInterviewId()))
			.isInstanceOf(IbEntityNotFoundException.class);
	}
}
