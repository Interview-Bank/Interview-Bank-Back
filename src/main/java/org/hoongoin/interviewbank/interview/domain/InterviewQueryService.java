package org.hoongoin.interviewbank.interview.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.exception.IbSoftDeleteException;
import org.hoongoin.interviewbank.interview.InterviewMapper;
import org.hoongoin.interviewbank.common.dto.PageDto;
import org.hoongoin.interviewbank.interview.enums.CareerYear;
import org.hoongoin.interviewbank.interview.enums.InterviewPeriod;
import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.repository.InterviewRepository;
import org.hoongoin.interviewbank.interview.application.entity.Interview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewQueryService {

	private final InterviewRepository interviewRepository;
	private final InterviewMapper interviewMapper;

	public Interview findInterviewById(long interviewId) {
		InterviewEntity interviewEntity = interviewRepository.findById(interviewId)
			.orElseThrow(() -> {
				log.info("Interview Not Found");
				return new IbEntityNotFoundException("Interview Not Found");
			});

		if (Boolean.TRUE.equals(interviewEntity.getDeletedFlag())) {
			throw new IbSoftDeleteException("Interview");
		}

		return interviewMapper.interviewEntityToInterview(interviewEntity, interviewEntity.getAccountEntity().getId());
	}

	public PageDto<Interview> findInterviewListByPageAndSize(int page, int size) {
		Page<InterviewEntity> interviewEntityPage = interviewRepository.findAllByPageableOrderByCreateTimeDesc(
			PageRequest.of(page, size));

		return getInterviews(interviewEntityPage);
	}

	public PageDto<Interview> searchInterview(String query, List<Long> jobCategoryIds, Date startDate, Date endDate,
		InterviewPeriod interviewPeriod, CareerYear careerYear, int page, int size) {
		Page<InterviewEntity> interviewEntityPage = interviewRepository.findAllByTitleAndJobCategoryIdsAndStartDateAndEndDatePageableOrderByCreateTimeDesc(
			query, jobCategoryIds, startDate, endDate, interviewPeriod, careerYear, PageRequest.of(page, size));

		return getInterviews(interviewEntityPage);
	}

	public PageDto<Interview> findInterviewsByAccountIdAndPageAndSize(long requestingAccountId, int page, int size) {
		Page<InterviewEntity> interviewPage = interviewRepository.findByAccountEntityIdAndDeleteFlag(
			PageRequest.of(page, size), requestingAccountId);

		return getInterviews(interviewPage);
	}

	private PageDto<Interview> getInterviews(Page<InterviewEntity> interviewEntityPage) {
		List<Interview> interviews = new ArrayList<>();

		interviewEntityPage.forEach(
			interviewEntity -> {
				if (Boolean.FALSE.equals(interviewEntity.getDeletedFlag())) {
					interviews.add(interviewMapper.interviewEntityToInterview(interviewEntity,
						interviewEntity.getAccountEntity().getId()));
				}
			});

		PageDto<Interview> interviewPageDto = new PageDto<>();
		interviewPageDto.setTotalPages(interviewEntityPage.getTotalPages());
		interviewPageDto.setTotalElements(interviewEntityPage.getTotalElements());
		interviewPageDto.setContent(interviews);
		return interviewPageDto;
	}
}
