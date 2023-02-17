package org.hoongoin.interviewbank.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IbEntityNotFoundException extends javax.persistence.EntityNotFoundException {

	public IbEntityNotFoundException(String message) {
		super(message + "is not found");
		log.info(message + "is not found");
	}
}