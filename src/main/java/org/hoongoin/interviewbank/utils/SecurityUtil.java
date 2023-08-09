package org.hoongoin.interviewbank.utils;

import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.config.IbUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtil {

	public static long getRequestingAccountId() {
		IbUserDetails userDetails = (IbUserDetails) SecurityContextHolder.getContext()
			.getAuthentication()
			.getPrincipal();
		return userDetails.getAccountId();
	}

	public static void setAuthentication(Account account) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(
			new IbUserDetails(account.getEmail(), account.getAccountId(), account.getPassword()),
			account.getPassword(),
			null);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
