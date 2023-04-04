package org.hoongoin.interviewbank.interview.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.exception.IbSoftDeleteException;
import org.hoongoin.interviewbank.interview.InterviewMapper;
import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.repository.InterviewRepository;
import org.hoongoin.interviewbank.interview.application.entity.Interview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterviewQueryService {

	private final InterviewRepository interviewRepository;
	private final InterviewMapper interviewMapper;
	private final AccountMapper accountMapper;

	public Interview findInterviewById(long interviewId) {
		InterviewEntity interviewEntity = interviewRepository.findById(interviewId)
			.orElseThrow(() -> new IbEntityNotFoundException("Interview"));

		if (Boolean.TRUE.equals(interviewEntity.getDeletedFlag())) {
			throw new IbSoftDeleteException("Interview");
		}

		return interviewMapper.interviewEntityToInterview(interviewEntity, interviewEntity.getAccountEntity().getId());
	}

	public List<Interview> findInterviewListByPageAndSize(int page, int size) {
		Page<InterviewEntity> interviewEntityPage = interviewRepository.findAllByPageableOrderByCreateTimeDesc(
			PageRequest.of(page, size));

		return getInterviews(interviewEntityPage);
	}

	public List<Interview> searchInterview(String query, Long jobCategoryId, Date startDate, Date endDate, int page,
		int size) {
		Page<InterviewEntity> interviewEntityPage = interviewRepository.findAllByTitleAndJobCategoryIdAndStartDateAndEndDatePageableOrderByCreateTimeAsc(
			query, jobCategoryId, startDate, endDate, PageRequest.of(page, size));

		return getInterviews(interviewEntityPage);
	}

	private List<Interview> getInterviews(Page<InterviewEntity> interviewEntityPage) {
		List<Interview> interviews = new ArrayList<>();

		interviewEntityPage.forEach(
			interviewEntity -> {
				if (Boolean.FALSE.equals(interviewEntity.getDeletedFlag())) {
					interviews.add(interviewMapper.interviewEntityToInterview(interviewEntity,
						interviewEntity.getAccountEntity().getId()));
				}
			});
		return interviews;
	}

	public List<Interview> findInterviewsByAccountIdAndPageAndSize(long requestingAccountId, int page, int size) {
		Page<InterviewEntity> interviewPage = interviewRepository.findByAccountEntityIdAndDeleteFlag(
			PageRequest.of(page, size), requestingAccountId);
		List<InterviewEntity> interviewEntities = interviewPage.getContent();

		List<Interview> interviews = new ArrayList<>();

		interviewEntities
			.forEach(interviewEntity -> interviews.add(interviewMapper.interviewEntityToInterview(interviewEntity,
				interviewEntity.getAccountEntity().getId())));
		return interviews;
	}
}
