package org.hoongoin.interviewbank.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageFactory {

	public static String createHttpMessage(HttpServletRequest request) {
		return String.format("REQUEST [%s] [%s]", request.getMethod(), request.getRequestURI());
	}

	public static String createStackTraceMessage(Exception exception) {
		var stackStream = new ByteArrayOutputStream();
		exception.printStackTrace(new PrintStream(stackStream));
		return stackStream.toString(StandardCharsets.UTF_8).substring(0, 1500);
	}
}
