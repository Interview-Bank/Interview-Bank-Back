package org.hoongoin.interviewbank.inquiry.application;

import java.io.IOException;
import java.util.Optional;

import org.hoongoin.interviewbank.common.discord.DiscordInquiryHandler;
import org.hoongoin.interviewbank.inquiry.application.entity.Inquiry;
import org.hoongoin.interviewbank.inquiry.controller.request.InquiryRequest;
import org.hoongoin.interviewbank.inquiry.domain.InquiryCommandService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class InquiryService {

	private final InquiryCommandService inquiryCommandService;
	private final DiscordInquiryHandler discordInquiryHandler;

	public void createInquiry(InquiryRequest inquiryRequest, Optional<MultipartFile> file) throws IOException {
		inquiryCommandService.createInquiry(
			Inquiry.builder()
				.content(inquiryRequest.getContent())
				.title(inquiryRequest.getTitle())
				.email(inquiryRequest.getEmail())
				.isAnswered(false)
				.build());

		discordInquiryHandler.send(inquiryRequest, file);
	}
}
