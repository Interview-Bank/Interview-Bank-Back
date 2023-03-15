package org.hoongoin.interviewbank.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IbInternalServerException extends RuntimeException {

	public IbInternalServerException(String message) {
		super(message + " Password Not Match");
		log.info(message + " Password Not Match");
	}
}
