package org.hoongoin.interviewbank.exception.handler;

import lombok.Getter;

@Getter
public class GlobalServerErrorResponse {

	public static final GlobalServerErrorResponse INTERNAL_SERVER_ERROR = new GlobalServerErrorResponse(
		"Internal Server Error");
	public static final GlobalServerErrorResponse NOT_FOUND = new GlobalServerErrorResponse("Not Found");
	public static final GlobalServerErrorResponse ACCESS_DENIED = new GlobalServerErrorResponse("Not Allowed User");

	private final String message;

	private GlobalServerErrorResponse(String message) {
		this.message = message;
	}

	public static GlobalServerErrorResponse from(String message) {
		return new GlobalServerErrorResponse(message);
	}
}
