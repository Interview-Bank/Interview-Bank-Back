package org.hoongoin.interviewbank.account.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RegisterRequest {

	private String nickname;
	private String email;
	private String password;
}
