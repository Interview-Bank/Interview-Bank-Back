package org.hoongoin.interviewbank.account.controller.request;

import lombok.Getter;

@Getter
public class ResetPasswordRequest {

	private String newPassword;
	private String newPasswordCheck;
}
