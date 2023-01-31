package org.hoongoin.interviewbank.scrap.service;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.account.service.domain.Account;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.interview.service.domain.Interview;
import org.hoongoin.interviewbank.scrap.ScrapMapper;
import org.hoongoin.interviewbank.scrap.entity.ScrapEntity;
import org.hoongoin.interviewbank.scrap.entity.ScrapQuestionEntity;
import org.hoongoin.interviewbank.scrap.repository.ScrapQuestionRepository;
import org.hoongoin.interviewbank.scrap.repository.ScrapRepository;
import org.hoongoin.interviewbank.scrap.service.domain.Scrap;
import org.hoongoin.interviewbank.scrap.service.domain.ScrapAndScrapQuestions;
import org.hoongoin.interviewbank.scrap.service.domain.ScrapQuestion;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ScrapCommandService {

	private final ScrapRepository scrapRepository;
	private final ScrapQuestionRepository scrapQuestionRepository;
	private final ScrapMapper scrapMapper;

	public ScrapAndScrapQuestions insertScrapAndScrapQuestions(Scrap scrap, List<ScrapQuestion> scrapQuestions,
		Account account, Interview interview) {
		ScrapEntity scrapEntity = scrapMapper.scrapToScrapEntity(scrap, account, interview);
		scrapRepository.save(scrapEntity);

		List<ScrapQuestion> savedScrapQuestions = new ArrayList<>();
		for (ScrapQuestion scrapQuestion : scrapQuestions) {
			ScrapQuestionEntity scrapQuestionEntity = ScrapQuestionEntity
				.builder()
				.scrapEntity(scrapEntity)
				.content(scrapQuestion.getContent())
				.build();
			scrapQuestionEntity = scrapQuestionRepository.save(scrapQuestionEntity);
			savedScrapQuestions.add(scrapMapper.scrapQuestionEntityToScrapQuestion(scrapQuestionEntity));
		}
		Scrap savedScrap = scrapMapper.scrapEntityToScrap(scrapEntity);
		return new ScrapAndScrapQuestions(savedScrap, savedScrapQuestions);
	}

	public boolean existsScrapByOriginalInterviewAndRequestingAccount(Interview originalInterview,
		Account requestingAccount) {
		return scrapRepository.existsByInterviewIdAndAccountId(originalInterview.getInterviewId(),
			requestingAccount.getAccountId());
	}

	public Scrap updateScrap(long scrapId, Scrap scrap) {
		ScrapEntity scrapEntity = scrapRepository.findById(scrapId)
			.orElseThrow(() -> new IbEntityNotFoundException("ScrapEntity"));
		scrapEntity.modifyEntity(scrap.getTitle());
		return scrapMapper.scrapEntityToScrap(scrapEntity);
	}

	public void deleteScrapById(long scrapId) {
		scrapRepository.deleteById(scrapId);
	}
}
