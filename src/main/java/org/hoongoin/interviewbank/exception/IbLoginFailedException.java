package org.hoongoin.interviewbank.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IbLoginFailedException extends RuntimeException {

	public IbLoginFailedException(String message) {
		super(message + " Login Failed");
		log.info(message + " Login Failed");
	}
}
