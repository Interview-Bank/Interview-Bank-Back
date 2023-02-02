package org.hoongoin.interviewbank.scrap.controller;

import org.hoongoin.interviewbank.scrap.controller.request.UpdateScrapAnswerRequest;
import org.hoongoin.interviewbank.scrap.controller.response.CreateScrapAnswerResponse;
import org.hoongoin.interviewbank.scrap.controller.response.UpdateScrapAnswerResponse;
import org.hoongoin.interviewbank.scrap.service.ScrapAnswerService;
import org.hoongoin.interviewbank.scrap.service.dto.ScrapAnswerIdsDto;
import org.hoongoin.interviewbank.scrap.service.dto.ScrapQuestionIdsDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

	@PostMapping()
	public ResponseEntity<CreateScrapAnswerResponse> createScrapAnswer(@PathVariable("scrap-id") long scrapId,
		@PathVariable("question-id") long scrapQuestionId) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		String requestingAccountOfEmail = (String)securityContext.getAuthentication().getPrincipal();

		ScrapQuestionIdsDto scrapQuestionIdsDto = new ScrapQuestionIdsDto(scrapId, scrapQuestionId);

		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_TYPE, "application/json")
			.body(scrapAnswerService.createScrapAnswerByScrapIdAndScrapQuestionId(scrapQuestionIdsDto,
				requestingAccountOfEmail));
	}

	@PutMapping("/{answer-id}")
	public ResponseEntity<UpdateScrapAnswerResponse> updateScrapAnswer(@PathVariable("scrap-id") long scrapId,
		@PathVariable("question-id") long scrapQuestionId, @PathVariable("answer-id") long scrapAnswerId,
		@RequestBody UpdateScrapAnswerRequest updateScrapAnswerRequest) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		String requestingAccountOfEmail = (String)securityContext.getAuthentication().getPrincipal();

		ScrapAnswerIdsDto scrapAnswerIdsDto = new ScrapAnswerIdsDto(scrapId, scrapQuestionId, scrapAnswerId);

		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_TYPE, "application/json")
			.body(scrapAnswerService.updateScrapAnswerByRequestAndSIds(updateScrapAnswerRequest, scrapAnswerIdsDto,
				requestingAccountOfEmail));
	}

	@DeleteMapping("{answer-id}")
	public ResponseEntity<Object> deleteScrapAnswer(@PathVariable("scrap-id") long scrapId,
		@PathVariable("question-id") long scrapQuestionId, @PathVariable("answer-id") long scrapAnswerId) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		String requestingAccountOfEmail = (String)securityContext.getAuthentication().getPrincipal();

		ScrapAnswerIdsDto scrapAnswerIdsDto = new ScrapAnswerIdsDto(scrapId, scrapQuestionId, scrapAnswerId);

		scrapAnswerService.deleteScrapAnswerByIds(scrapAnswerIdsDto, requestingAccountOfEmail);

		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_TYPE, "application/json")
			.body("Success");
	}
}
