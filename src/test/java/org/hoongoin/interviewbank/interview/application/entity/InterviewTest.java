package org.hoongoin.interviewbank.interview.application.entity;

import static org.assertj.core.api.Assertions.*;

import org.hoongoin.interviewbank.exception.IbValidationException;
import org.junit.jupiter.api.Test;

class InterviewTest {

	@Test
	void validateTitle_Success_TitleLengthIs128Byte() {
		//given
		String englishTitle = "hi my name is hunkhi my name is hunkhi my name is hunkhi my name is hunkhi my name is hunkhi my name is hunkhi my name is hunki.";
		String koreanTitle = "훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈 기.";

		//when //then
		assertThat(Interview.builder().title(englishTitle).build().getTitle()).isEqualTo(englishTitle);
		assertThat(Interview.builder().title(koreanTitle).build().getTitle()).isEqualTo(koreanTitle);

	}

	@Test
	void validateTitle_Success_TitleLengthIs1Byte() {
		//given
		String title = "h";

		//when //then
		assertThat(Interview.builder().title(title).accountId(1L).build().getTitle()).isEqualTo(title);

	}

	@Test
	void validateTitle_Fail_TitleLengthIs129Byte() {
		//given
		String englishTitle = "hi my name is hunkhi my name is hunkhi my name is hunkhi my name is hunkhi my name is hunkhi my name is hunkhi my name is hunki..";
		String koreanTitle = "훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈";

		//when //then
		assertThatThrownBy(() -> Interview.builder().title(englishTitle).build())
			.isInstanceOf(IbValidationException.class)
			.hasMessage("title");
		assertThatThrownBy(() -> Interview.builder().title(koreanTitle).build())
			.isInstanceOf(IbValidationException.class)
			.hasMessage("title");

	}
}
