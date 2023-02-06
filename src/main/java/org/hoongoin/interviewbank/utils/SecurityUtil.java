package org.hoongoin.interviewbank.utils;

import org.hoongoin.interviewbank.config.IbUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtil {

	public static String getRequestingAccountOfEmail() {
		IbUserDetails userDetails = (IbUserDetails)SecurityContextHolder.getContext()
			.getAuthentication()
			.getPrincipal();
		return userDetails.getUsername();
	}

	public static long getRequestingAccountId() {
		IbUserDetails userDetails = (IbUserDetails)SecurityContextHolder.getContext()
			.getAuthentication()
			.getPrincipal();
		return userDetails.getAccountId();
	}
}
