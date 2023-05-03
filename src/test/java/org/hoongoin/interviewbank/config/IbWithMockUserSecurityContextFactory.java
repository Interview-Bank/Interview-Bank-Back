package org.hoongoin.interviewbank.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class IbWithMockUserSecurityContextFactory implements WithSecurityContextFactory<IbWithMockUser> {

	@Override
	public SecurityContext createSecurityContext(IbWithMockUser annotation) {

		final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

		final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
			new IbUserDetails("gnsrl76@naver.com", 1L, "hunki123"), null, null);

		securityContext.setAuthentication(authenticationToken);
		return securityContext;
	}
}
