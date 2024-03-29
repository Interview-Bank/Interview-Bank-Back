package org.hoongoin.interviewbank.account.controller.response;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetMeResponse {

	private String nickname;
	private String email;
	private LocalDate passwordUpdatedAt;
	private String imageUrl;
}
