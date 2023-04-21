package org.hoongoin.interviewbank.interview.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CareerYear {

	NEWCOMER("신입"),
	ONE_YEAR("1년차"),
	TWO_YEAR("2년차"),
	THREE_YEAR("3년차"),
	FOUR_YEAR("4년차"),
	FIVE_YEAR("5년차"),
	SIX_YEAR("6년차"),
	SEVEN_YEAR("7년차"),
	EIGHT_YEAR("8년차"),
	NINE_YEAR("9년차"),
	TEN_YEAR("10년차"),
	ETC("기타");

	private final String koreanName;
}
