package org.hoongoin.interviewbank.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IbBadRequestException extends RuntimeException {

	public IbBadRequestException(String message) {
		super(message + " Bad Request");
		log.info(message + " Bad Request");
	}
}