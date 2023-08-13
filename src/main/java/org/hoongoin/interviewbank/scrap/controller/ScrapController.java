package org.hoongoin.interviewbank.scrap.controller;

import static org.hoongoin.interviewbank.utils.SecurityUtil.*;

import org.hoongoin.interviewbank.config.IbUserDetails;
import org.hoongoin.interviewbank.scrap.controller.request.CreateScrapRequest;
import org.hoongoin.interviewbank.scrap.controller.request.UpdateScrapRequest;
import org.hoongoin.interviewbank.scrap.controller.response.CreateScrapResponse;
import org.hoongoin.interviewbank.scrap.controller.response.ReadScrapAllOfInterview;
import org.hoongoin.interviewbank.scrap.controller.response.ReadScrapDetailResponse;
import org.hoongoin.interviewbank.scrap.controller.response.ReadScrapPageResponse;
import org.hoongoin.interviewbank.scrap.controller.response.UpdateScrapResponse;
import org.hoongoin.interviewbank.scrap.application.ScrapService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
	public ResponseEntity<ReadScrapPageResponse> readScrapAll(
		@RequestParam(name = "page", defaultValue = "0") int page,
		@RequestParam(name = "size", defaultValue = "10") int size) {
		long requestingAccountId = getRequestingAccountId();
		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_TYPE, "application/json")
			.body(scrapService.readScrapByRequestingAccountIdAndPageAndSize(requestingAccountId, page, size));
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

	@DeleteMapping("/{scrap-id}")
	public ResponseEntity<Void> deleteScrap(@PathVariable("scrap-id") long scrapId) {
		long requestingAccountId = getRequestingAccountId();
		scrapService.deleteScrapByScrapIdAndRequestingAccountId(scrapId, requestingAccountId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/interview/{interview-id}")
	public ResponseEntity<ReadScrapAllOfInterview> readScrapAllOfInterview(
		@PathVariable("interview-id") long interviewId,
		@RequestParam(name = "page", defaultValue = "0") int page,
		@RequestParam(name = "size", defaultValue = "10") int size) {
		Long requestingAccountId = null;
		if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof IbUserDetails){
			requestingAccountId = getRequestingAccountId();
		}

		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_TYPE, "application/json")
			.body(scrapService.readScrapAllOfInterview(requestingAccountId, interviewId, page, size));
	}
}
