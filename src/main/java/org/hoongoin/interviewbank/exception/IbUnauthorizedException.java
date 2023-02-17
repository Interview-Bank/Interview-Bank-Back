package org.hoongoin.interviewbank.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IbUnauthorizedException extends RuntimeException{

	public IbUnauthorizedException(String message){
		super(message);
		log.info(message + "Unauthorized exception");
	}
}
