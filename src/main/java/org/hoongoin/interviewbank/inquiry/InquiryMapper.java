package org.hoongoin.interviewbank.inquiry;

import org.hoongoin.interviewbank.inquiry.application.entity.Inquiry;
import org.hoongoin.interviewbank.inquiry.infrastructure.entity.InquiryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InquiryMapper {

	InquiryEntity inquiryToInquiryEntity(Inquiry inquiry);
	Inquiry inquiryEntityToInquiry(InquiryEntity inquiryEntity);
}
