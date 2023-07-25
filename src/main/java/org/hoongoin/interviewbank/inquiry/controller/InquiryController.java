package org.hoongoin.interviewbank.inquiry.controller;

import java.io.IOException;
import java.util.Optional;

import org.hoongoin.interviewbank.inquiry.application.InquiryService;
import org.hoongoin.interviewbank.inquiry.controller.request.InquiryRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/inquiry")
public class InquiryController {

	private final InquiryService inquiryService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> createInquiry(
		@RequestPart("inquiryRequest") InquiryRequest inquiryRequest,
		@RequestPart(value = "file", required = false) Optional<MultipartFile> file) throws IOException {
		//TODO: file upload to S3
		inquiryService.createInquiry(inquiryRequest, file);
		return ResponseEntity.ok().body(null);
	}
}
