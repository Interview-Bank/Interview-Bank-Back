package org.hoongoin.interviewbank.exception.handler;

import javax.servlet.http.HttpServletRequest;

import org.hoongoin.interviewbank.common.discord.DiscordHandler;
import org.hoongoin.interviewbank.exception.IbAccountNotMatchException;
import org.hoongoin.interviewbank.exception.IbBadRequestException;
import org.hoongoin.interviewbank.exception.IbEntityExistsException;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.exception.IbInternalServerException;
import org.hoongoin.interviewbank.exception.IbSoftDeleteException;
import org.hoongoin.interviewbank.exception.IbUnauthorizedException;
import org.hoongoin.interviewbank.exception.IbValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class IbControllerAdvice {

	private final DiscordHandler discordHandler;

	@ExceptionHandler({IbBadRequestException.class, IbAccountNotMatchException.class, IbValidationException.class,
		IbEntityExistsException.class})
	public ResponseEntity<Object> handleBadRequestException(Exception exception, HttpServletRequest request) {
		discordHandler.send(exception, request);
		return ResponseEntity
			.status(IbErrorCode.BAD_REQUEST.getHttpStatus())
			.body(exception.getMessage());
	}

	@ExceptionHandler({IbEntityNotFoundException.class, IbSoftDeleteException.class})
	public ResponseEntity<Object> handleIbEntityNotFoundException(Exception exception,
		HttpServletRequest request) {
		discordHandler.send(exception, request);
		return ResponseEntity
			.status(IbErrorCode.NOT_FOUND.getHttpStatus())
			.body(exception.getMessage());
	}

	@ExceptionHandler(IbUnauthorizedException.class)
	public ResponseEntity<Object> handleIbUnauthorizedException(Exception exception, HttpServletRequest request) {
		discordHandler.send(exception, request);
		return ResponseEntity
			.status(IbErrorCode.UNAUTHORIZED.getHttpStatus())
			.body(exception.getMessage());
	}

	@ExceptionHandler({IbInternalServerException.class})
	public ResponseEntity<Object> handleIbInternalServerException(IbInternalServerException exception,
		HttpServletRequest request) {
		discordHandler.send(exception, request);
		return ResponseEntity
			.status(IbErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
			.body(exception.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUnhandledException(Exception exception,
		HttpServletRequest request) {
		discordHandler.send(exception, request);
		return ResponseEntity
			.status(IbErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
			.body("Internal Server Error");
	}
}
