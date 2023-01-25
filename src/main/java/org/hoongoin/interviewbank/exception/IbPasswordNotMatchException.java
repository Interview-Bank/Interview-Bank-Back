package org.hoongoin.interviewbank.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IbPasswordNotMatchException extends RuntimeException{
	public IbPasswordNotMatchException(String email) {
		super(email + "password not match");
		log.error(email + "password not match");
	}
}
