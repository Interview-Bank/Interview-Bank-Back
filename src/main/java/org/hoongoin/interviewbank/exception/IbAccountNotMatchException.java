package org.hoongoin.interviewbank.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IbAccountNotMatchException extends RuntimeException {

	public IbAccountNotMatchException(String message) {
		super(message + " Do Not Match");
		log.info(message + " Do Not Match");
	}
}
