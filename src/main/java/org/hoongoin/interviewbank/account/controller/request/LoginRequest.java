package org.hoongoin.interviewbank.account.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest {

	private String email;
	private String password;
}
