package org.hoongoin.interviewbank.exception;

import javax.persistence.EntityExistsException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IbEntityExistsException extends EntityExistsException {
	public IbEntityExistsException(String message) {
		super(message + "already exists");
		log.error(message + "already exists");
	}
}