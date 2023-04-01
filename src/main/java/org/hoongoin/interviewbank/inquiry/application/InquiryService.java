package org.hoongoin.interviewbank.inquiry.application;

import org.hoongoin.interviewbank.inquiry.application.entity.Inquiry;
import org.hoongoin.interviewbank.inquiry.controller.request.InquiryRequest;
import org.hoongoin.interviewbank.inquiry.domain.InquiryCommandService;
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
				.isAnswered(false)
				.build());
	}
}
