package org.hoongoin.interviewbank.inquiry.infrastructure;

import org.hoongoin.interviewbank.inquiry.infrastructure.entity.InquiryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends JpaRepository<InquiryEntity, Long> {
}
