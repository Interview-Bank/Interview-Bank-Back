package org.hoongoin.interviewbank.account.controller.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterRequest {

	@Size(min=4, max=30)
	private String nickname;
	@NotNull @Email
	private String email;
	@NotNull
	private String password;
}
