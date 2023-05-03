package org.hoongoin.interviewbank.scrap.controller;

import static org.hoongoin.interviewbank.utils.SecurityUtil.*;

import org.hoongoin.interviewbank.scrap.controller.request.UpdateScrapAnswerRequest;
import org.hoongoin.interviewbank.scrap.controller.response.UpdateScrapAnswerResponse;
import org.hoongoin.interviewbank.scrap.application.ScrapAnswerService;
import org.hoongoin.interviewbank.scrap.application.dto.ScrapAnswerIdsDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/scraps/{scrap-id}/questions/{question-id}/answers")
public class ScrapAnswerController {

	private final ScrapAnswerService scrapAnswerService;

	@PutMapping("/{answer-id}")
	public ResponseEntity<UpdateScrapAnswerResponse> updateScrapAnswer(@PathVariable("scrap-id") long scrapId,
		@PathVariable("question-id") long scrapQuestionId, @PathVariable("answer-id") long scrapAnswerId,
		@RequestBody UpdateScrapAnswerRequest updateScrapAnswerRequest) {
		long requestingAccountId = getRequestingAccountId();

		ScrapAnswerIdsDto scrapAnswerIdsDto = new ScrapAnswerIdsDto(scrapId, scrapQuestionId, scrapAnswerId);

		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_TYPE, "application/json")
			.body(scrapAnswerService.updateScrapAnswerByRequestAndSIds(updateScrapAnswerRequest, scrapAnswerIdsDto,
				requestingAccountId));
	}
}
