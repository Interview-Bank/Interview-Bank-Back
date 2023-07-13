package org.hoongoin.interviewbank.config;

import java.lang.reflect.Method;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IbAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {

	@Override
	public void handleUncaughtException(Throwable ex, Method method, Object... params) {
		StringBuilder exceptionMessage = new StringBuilder();
		exceptionMessage.append("Exception occurred in method: ").append(method.getName());
		exceptionMessage.append(", with exception message: ").append(ex.getMessage());
		exceptionMessage.append(". With parameters: ");
		for (int i = 0; i < params.length; i++) {
			exceptionMessage.append("[Parameter ").append(i+1).append(": ").append(params[i]).append("]");
			if (i < params.length - 1) {
				exceptionMessage.append(", ");
			}
		}
		log.error(exceptionMessage.toString());
	}
}
