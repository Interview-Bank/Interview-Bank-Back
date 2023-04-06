package org.hoongoin.interviewbank.account.controller.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ModifyNicknameResponse {

	private String nickname;
	private String email;
	private LocalDateTime passwordUpdatedAt;
}
