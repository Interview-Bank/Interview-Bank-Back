package org.hoongoin.interviewbank.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IbValidationException extends RuntimeException{

	public IbValidationException(String message){
		super(message);
		log.error(message + "validation exception");
	}
}
