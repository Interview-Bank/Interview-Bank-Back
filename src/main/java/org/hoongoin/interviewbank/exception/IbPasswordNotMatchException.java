package org.hoongoin.interviewbank.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IbPasswordNotMatchException extends RuntimeException {

	public IbPasswordNotMatchException(String email) {
		super(email + " Password Not Match");
		log.info(email + " Password Not Match");
	}
}
