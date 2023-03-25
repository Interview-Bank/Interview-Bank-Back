package org.hoongoin.interviewbank.inquiry;

import org.hoongoin.interviewbank.inquiry.application.Inquiry;
import org.hoongoin.interviewbank.inquiry.infrastructure.InquiryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InquiryMapper {

	InquiryEntity inquiryToInquiryEntity(Inquiry inquiry);
	Inquiry inquiryEntityToInquiry(InquiryEntity inquiryEntity);
}
