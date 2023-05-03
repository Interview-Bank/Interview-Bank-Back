package org.hoongoin.interviewbank.exception;

import javax.persistence.EntityExistsException;

public class IbEntityExistsException extends EntityExistsException {

	public IbEntityExistsException(String message) {
		super(message);
	}
}
