package org.hoongoin.interviewbank.exception;

public class IbEntityNotFoundException extends javax.persistence.EntityNotFoundException {

	public IbEntityNotFoundException(String message) {
		super(message);
	}
}
