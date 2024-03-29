package org.hoongoin.interviewbank.interview.application.entity;

import static org.assertj.core.api.Assertions.*;

import org.hoongoin.interviewbank.exception.IbValidationException;
import org.junit.jupiter.api.Test;

class QuestionTest {

	@Test
	void setContent_Success_ContentLengthIs100000Byte() {
		//given
		String content = "a".repeat(100000);

		//when
		Question question = Question.builder().questionId(1L).interviewId(1L).content(content).build();

		//when
		assertThat(question.getContent()).isEqualTo(content);

	}

	@Test
	void setContent_Fail_ContentLengthIs1000001Byte() {
		//given
		String content = "a".repeat(100001);

		//when //then
		assertThatThrownBy(() -> Question.builder().questionId(1L).interviewId(1L).content(content).build())
			.hasMessage("Question Validation Failed")
			.isInstanceOf(IbValidationException.class);
	}

	@Test
	void setContent_Fail_ContentLengthIs0Byte() {
		//given
		String content = "";

		//when //then
		assertThatThrownBy(() -> Question.builder().questionId(1L).interviewId(1L).content(content).build())
			.hasMessage("Question Validation Failed")
			.isInstanceOf(IbValidationException.class);
	}
}
