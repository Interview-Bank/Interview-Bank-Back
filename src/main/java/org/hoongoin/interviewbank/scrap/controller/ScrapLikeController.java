package org.hoongoin.interviewbank.scrap.controller;

import org.hoongoin.interviewbank.scrap.application.ScrapLikeService;
import org.hoongoin.interviewbank.scrap.controller.request.LikeScrapRequest;
import org.hoongoin.interviewbank.scrap.controller.response.LikeScrapResponse;
import org.hoongoin.interviewbank.utils.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("scraps")
@RequiredArgsConstructor
public class ScrapLikeController {

	private final ScrapLikeService scrapLikeService;

	@PostMapping("/{scrap-id}/like")
	public ResponseEntity<LikeScrapResponse> likeScrap(@PathVariable("scrap-id") long scrapId,
		@RequestBody LikeScrapRequest likeScrapRequest) {
		long requestingAccountId = SecurityUtil.getRequestingAccountId();
		LikeScrapResponse likeScrapResponse = scrapLikeService.likeScrap(scrapId,
			likeScrapRequest, requestingAccountId);
		return ResponseEntity.ok().body(likeScrapResponse);
	}
}
