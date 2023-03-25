package org.hoongoin.interviewbank.scrap.application.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScrapWithScrapQuestionAndScrapAnswerList {

	private Scrap scrap;
	private List<ScrapQuestionAndScrapAnswer> scrapQuestionAndScrapAnswerList;
}
