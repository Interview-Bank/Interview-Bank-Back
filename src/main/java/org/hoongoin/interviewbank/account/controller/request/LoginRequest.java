package org.hoongoin.interviewbank.account.controller.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class LoginRequest {

	@NotNull @Email
	private String email;
	@NotNull
	private String password;
}
