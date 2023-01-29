package org.hoongoin.interviewbank.interview.service.domain;

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
		assertThat(new Interview(englishTitle).getTitle()).isEqualTo(englishTitle);
		assertThat(new Interview(koreanTitle).getTitle()).isEqualTo(koreanTitle);

	}

	@Test
	void validateTitle_Success_TitleLengthIs1Byte() {
		//given
		String title = "h";

		//when //then
		assertThat(new Interview(title, 1L).getTitle()).isEqualTo(title);

	}

	@Test
	void validateTitle_Fail_TitleLengthIs129Byte() {
		//given
		String englishTitle = "hi my name is hunkhi my name is hunkhi my name is hunkhi my name is hunkhi my name is hunkhi my name is hunkhi my name is hunki..";
		String koreanTitle = "훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈기훈";

		//when //then
		assertThatThrownBy(() -> new Interview(englishTitle))
			.isInstanceOf(IbValidationException.class)
			.hasMessage("title");
		assertThatThrownBy(() -> new Interview(englishTitle))
			.isInstanceOf(IbValidationException.class)
			.hasMessage("title");

	}


}