package org.hoongoin.interviewbank.inquiry.controller;

import org.hoongoin.interviewbank.account.controller.request.RegisterRequest;
import org.hoongoin.interviewbank.account.controller.response.RegisterResponse;
import org.hoongoin.interviewbank.inquiry.application.InquiryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/inquiry")
public class InquiryController {

	private final InquiryService inquiryService;

	@PostMapping
	public ResponseEntity<Object> createInquiry(@RequestBody InquiryRequest inquiryRequest) {
		inquiryService.createInquiry(inquiryRequest);
		return ResponseEntity.ok().body(null);
	}
}
