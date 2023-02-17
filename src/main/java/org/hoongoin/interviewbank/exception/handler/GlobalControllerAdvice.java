package org.hoongoin.interviewbank.exception.handler;

import javax.servlet.http.HttpServletRequest;

import org.hoongoin.interviewbank.common.discord.DiscordHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

	private final DiscordHandler discordHandler;

	@ExceptionHandler(Exception.class)
	public ResponseEntity<GlobalServerErrorResponse> handleUnhandledException(Exception exception,
		HttpServletRequest request) {
		discordHandler.send(exception, request);
		return ResponseEntity.internalServerError().body(GlobalServerErrorResponse.INTERNAL_SERVER_ERROR);
	}
}
