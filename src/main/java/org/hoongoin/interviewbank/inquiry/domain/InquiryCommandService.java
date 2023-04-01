package org.hoongoin.interviewbank.inquiry.domain;

import org.hoongoin.interviewbank.inquiry.InquiryMapper;
import org.hoongoin.interviewbank.inquiry.application.entity.Inquiry;
import org.hoongoin.interviewbank.inquiry.infrastructure.entity.InquiryEntity;
import org.hoongoin.interviewbank.inquiry.infrastructure.InquiryRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class InquiryCommandService {

	private final InquiryRepository inquiryRepository;
	private final InquiryMapper inquiryMapper;

	public Inquiry createInquiry(Inquiry inquiry) {
		InquiryEntity inquiryEntity = inquiryMapper.inquiryToInquiryEntity(inquiry);
		inquiryRepository.save(inquiryEntity);
		return inquiryMapper.inquiryEntityToInquiry(inquiryEntity);
	}
}
