package org.hoongoin.interviewbank.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IbSoftDeleteException extends RuntimeException {

	public IbSoftDeleteException(String message) {
		super(message + " Is Deleted Data");
		log.info(message + " Do Deleted Data");
	}
}
