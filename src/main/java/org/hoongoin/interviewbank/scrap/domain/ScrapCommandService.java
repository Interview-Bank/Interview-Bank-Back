package org.hoongoin.interviewbank.scrap.domain;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.interview.application.entity.Interview;
import org.hoongoin.interviewbank.scrap.ScrapMapper;
import org.hoongoin.interviewbank.scrap.application.entity.ScrapQuestionAndScrapAnswer;
import org.hoongoin.interviewbank.scrap.application.entity.ScrapWithScrapQuestionAndScrapAnswerList;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapAnswerEntity;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapEntity;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapQuestionEntity;
import org.hoongoin.interviewbank.scrap.infrastructure.repository.ScrapAnswerRepository;
import org.hoongoin.interviewbank.scrap.infrastructure.repository.ScrapQuestionRepository;
import org.hoongoin.interviewbank.scrap.infrastructure.repository.ScrapRepository;
import org.hoongoin.interviewbank.scrap.application.entity.Scrap;
import org.hoongoin.interviewbank.scrap.application.entity.ScrapQuestion;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScrapCommandService {

	private final ScrapRepository scrapRepository;
	private final ScrapQuestionRepository scrapQuestionRepository;
	private final ScrapAnswerRepository scrapAnswerRepository;
	private final ScrapMapper scrapMapper;

	public ScrapWithScrapQuestionAndScrapAnswerList insertScrapAndScrapQuestions(Scrap scrap,
		List<ScrapQuestion> scrapQuestions, Account account, Interview interview) {
		ScrapEntity scrapEntity = scrapMapper.scrapToScrapEntity(scrap, account, interview);
		scrapRepository.save(scrapEntity);

		List<ScrapQuestionAndScrapAnswer> scrapQuestionAndScrapAnswerList = new ArrayList<>();
		for (ScrapQuestion scrapQuestion : scrapQuestions) {
			ScrapQuestionEntity scrapQuestionEntity = ScrapQuestionEntity.builder()
				.scrapEntity(scrapEntity)
				.content(scrapQuestion.getContent())
				.gptAnswer(scrapQuestion.getGptAnswer())
				.build();
			scrapQuestionEntity = scrapQuestionRepository.save(scrapQuestionEntity);
			ScrapAnswerEntity scrapAnswerEntity = ScrapAnswerEntity.builder()
				.scrapQuestionEntity(scrapQuestionEntity)
				.build();
			scrapAnswerRepository.save(scrapAnswerEntity);
			scrapQuestionAndScrapAnswerList.add(
				new ScrapQuestionAndScrapAnswer(scrapMapper.scrapQuestionEntityToScrapQuestion(scrapQuestionEntity),
					scrapMapper.scrapAnswerEntityToScrapAnswer(scrapAnswerEntity)));
		}
		Scrap savedScrap = scrapMapper.scrapEntityToScrap(scrapEntity);
		return new ScrapWithScrapQuestionAndScrapAnswerList(savedScrap, scrapQuestionAndScrapAnswerList);
	}

	public Scrap updateScrap(Scrap scrap) {
		ScrapEntity scrapEntity = scrapRepository.findById(scrap.getScrapId()).orElseThrow(() -> {
			log.info("Scrap Not Found");
			return new IbEntityNotFoundException("Scrap Not Found");
		});
		scrapEntity.modifyEntity(scrap.getTitle());
		scrapEntity.setIsPublic(scrap.getIsPublic());
		return scrapMapper.scrapEntityToScrap(scrapEntity);
	}

	public void deleteScrapById(long scrapId) {
		scrapRepository.deleteById(scrapId);
	}
}
