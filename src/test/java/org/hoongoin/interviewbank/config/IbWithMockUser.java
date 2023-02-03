package org.hoongoin.interviewbank.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = IbWithMockUserSecurityContextFactory.class)
public @interface IbWithMockUser {

	String username() default "gnsrl76@naver.com";

	long accountId() default 1L;
}
