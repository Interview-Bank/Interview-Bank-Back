package org.hoongoin.interviewbank.scrap.controller;

import org.hoongoin.interviewbank.scrap.controller.request.CreateScrapRequest;
import org.hoongoin.interviewbank.scrap.controller.request.UpdateScrapRequest;
import org.hoongoin.interviewbank.scrap.controller.response.CreateScrapResponse;
import org.hoongoin.interviewbank.scrap.controller.response.ReadScrapResponse;
import org.hoongoin.interviewbank.scrap.controller.response.UpdateScrapResponse;
import org.hoongoin.interviewbank.scrap.service.ScrapService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/scraps")
public class ScrapController {

	private final ScrapService scrapService;

	@PostMapping
	public ResponseEntity<CreateScrapResponse> createScrap(@RequestBody CreateScrapRequest createScrapRequest){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		String requestingAccountOfEmail = (String) securityContext.getAuthentication().getPrincipal();
		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_TYPE, "application/json")
			.body(scrapService.createScrapByCreateRequest(createScrapRequest, requestingAccountOfEmail));
	}

	@PutMapping("/{scrap-id}")
	public ResponseEntity<UpdateScrapResponse> updateScrap(@RequestBody UpdateScrapRequest updateScrapRequest,
		@PathVariable("scrap-id") long scrapId){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		String requestingAccountOfEmail = (String) securityContext.getAuthentication().getPrincipal();
		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_TYPE, "application/json")
			.body(scrapService.updateScrapByRequestAndScrapId(updateScrapRequest, scrapId, requestingAccountOfEmail));
	}

	@DeleteMapping("/{scrap-id}")
	public ResponseEntity<Object> deleteScrap(@PathVariable("scrap-id") long scrapId){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		String requestingAccountOfEmail = (String) securityContext.getAuthentication().getPrincipal();
		scrapService.deleteScrapByRequestAndScrapId(scrapId, requestingAccountOfEmail);
		return ResponseEntity.ok().body("Success");
	}

	@GetMapping("/{scrap-id}")
	public ResponseEntity<ReadScrapResponse> readScrap(@PathVariable("scrap-id") long scrapId){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		String requestingAccountOfEmail = (String) securityContext.getAuthentication().getPrincipal();
		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_TYPE, "application/json")
			.body(scrapService.readScrapById(scrapId, requestingAccountOfEmail));
	}
}
