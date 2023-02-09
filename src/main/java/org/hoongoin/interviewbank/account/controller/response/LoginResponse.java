package org.hoongoin.interviewbank.account.controller.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

	private long accountId;
	private String nickname;
	private String email;
}
