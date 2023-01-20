package org.hoongoin.interviewbank.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomEntityNotFoundException extends javax.persistence.EntityNotFoundException {
	public CustomEntityNotFoundException(String message) {
		super(message + "is not found");
		log.error(message + "is not found");
	}
}