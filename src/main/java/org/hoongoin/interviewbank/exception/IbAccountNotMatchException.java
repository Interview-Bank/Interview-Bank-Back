package org.hoongoin.interviewbank.exception;

public class IbAccountNotMatchException extends RuntimeException {

	public IbAccountNotMatchException(String message) {
		super(message + " Do Not Match");
	}
}
