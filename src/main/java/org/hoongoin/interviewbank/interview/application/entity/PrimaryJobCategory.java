package org.hoongoin.interviewbank.interview.application.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PrimaryJobCategory {

	DEVELOP("개발"),
	R_N_D("R&D"),
	DESIGN("디자인"),
	PLANNING("기획/PM"),
	MARKETING("마케팅"),
	ETC("기타");

	private final String primaryJobName;
}
