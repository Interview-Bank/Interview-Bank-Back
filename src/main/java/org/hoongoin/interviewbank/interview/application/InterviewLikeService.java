package org.hoongoin.interviewbank.interview.application;

import java.util.Optional;

import org.hoongoin.interviewbank.interview.application.entity.InterviewLike;
import org.hoongoin.interviewbank.interview.controller.request.LikeInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.response.LikeInterviewResponse;
import org.hoongoin.interviewbank.interview.domain.InterviewLikeCommandService;
import org.hoongoin.interviewbank.interview.domain.InterviewLikeQueryService;
import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewLikeEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class InterviewLikeService {

	private final InterviewLikeCommandService interviewLikeCommandService;
	private final InterviewLikeQueryService interviewLikeQueryService;

	@Transactional
	public LikeInterviewResponse likeInterview(long interviewId, LikeInterviewRequest likeInterviewRequest, long requestingAccountId) {
		Optional<InterviewLikeEntity> optionalInterviewLikeEntity = interviewLikeQueryService.findByAccountIdAndInterviewId(
			requestingAccountId, interviewId);

		LikeInterviewResponse likeInterviewResponse;
		if (optionalInterviewLikeEntity.isPresent()) {
			InterviewLikeEntity interviewLikeEntity = optionalInterviewLikeEntity.get();
			interviewLikeEntity.modifyLike(likeInterviewRequest.getLike());
			likeInterviewResponse = new LikeInterviewResponse(interviewLikeEntity.isLike());
		} else {
			InterviewLike interviewLike = InterviewLike.builder()
				.interviewLikeId(interviewId)
				.accountId(requestingAccountId)
				.like(likeInterviewRequest.getLike())
				.build();
			interviewLike = interviewLikeCommandService.insertInterviewLike(interviewLike);
			likeInterviewResponse = new LikeInterviewResponse(interviewLike.getLike());
		}

		return likeInterviewResponse;
	}
}
