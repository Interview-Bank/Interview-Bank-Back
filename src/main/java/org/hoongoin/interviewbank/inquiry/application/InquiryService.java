package org.hoongoin.interviewbank.inquiry.application;

import org.hoongoin.interviewbank.inquiry.controller.InquiryRequest;
import org.hoongoin.interviewbank.inquiry.controller.InquiryResponse;
import org.hoongoin.interviewbank.inquiry.domain.InquiryCommandService;
import org.hoongoin.interviewbank.interview.domain.InterviewCommandService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class InquiryService {

	private final InquiryCommandService inquiryCommandService;

	public void createInquiry(InquiryRequest inquiryRequest) {
		inquiryCommandService.createInquiry(
			Inquiry.builder()
				.content(inquiryRequest.getContent())
				.title(inquiryRequest.getTitle())
				.email(inquiryRequest.getEmail())
				.build());
	}
}
