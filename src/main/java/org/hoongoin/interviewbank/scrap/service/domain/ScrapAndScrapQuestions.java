package org.hoongoin.interviewbank.scrap.service.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScrapAndScrapQuestions {
	private Scrap scrap;
	private List<ScrapQuestion> scrapQuestionList;
}
