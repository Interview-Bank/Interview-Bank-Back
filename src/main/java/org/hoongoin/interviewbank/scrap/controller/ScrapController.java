package org.hoongoin.interviewbank.scrap.controller;

import static org.hoongoin.interviewbank.utils.SecurityUtil.*;

import java.util.List;

import org.hoongoin.interviewbank.scrap.controller.request.CreateScrapRequest;
import org.hoongoin.interviewbank.scrap.controller.request.UpdateScrapRequest;
import org.hoongoin.interviewbank.scrap.controller.response.CreateScrapResponse;
import org.hoongoin.interviewbank.scrap.controller.response.ReadScrapDetailResponse;
import org.hoongoin.interviewbank.scrap.controller.response.ReadScrapResponse;
import org.hoongoin.interviewbank.scrap.controller.response.UpdateScrapResponse;
import org.hoongoin.interviewbank.scrap.application.ScrapService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/scraps")
public class ScrapController {

	private final ScrapService scrapService;

	@PostMapping
	public ResponseEntity<CreateScrapResponse> createScrap(@RequestBody CreateScrapRequest createScrapRequest) {
		long requestingAccountId = getRequestingAccountId();
		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_TYPE, "application/json")
			.body(scrapService.createScrapByCreateRequest(createScrapRequest, requestingAccountId));
	}

	@GetMapping
	public ResponseEntity<List<ReadScrapResponse>> readScrapAll(
		@RequestParam(name = "page", defaultValue = "0") int page,
		@RequestParam(name = "size", defaultValue = "10") int size) {
		long requestingAccountId = getRequestingAccountId();
		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_TYPE, "application/json")
			.body(scrapService.readScrapAll(requestingAccountId, page, size));
	}

	@GetMapping("/{scrap-id}")
	public ResponseEntity<ReadScrapDetailResponse> readScrapDetail(@PathVariable("scrap-id") long scrapId) {
		long requestingAccountId = getRequestingAccountId();
		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_TYPE, "application/json")
			.body(scrapService.readScrapDetailById(scrapId, requestingAccountId));
	}

	@PutMapping("/{scrap-id}")
	public ResponseEntity<UpdateScrapResponse> updateScrap(@RequestBody UpdateScrapRequest updateScrapRequest,
		@PathVariable("scrap-id") long scrapId) {
		long requestingAccountId = getRequestingAccountId();
		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_TYPE, "application/json")
			.body(scrapService.updateScrapByRequestAndScrapId(updateScrapRequest, scrapId, requestingAccountId));
	}
}
