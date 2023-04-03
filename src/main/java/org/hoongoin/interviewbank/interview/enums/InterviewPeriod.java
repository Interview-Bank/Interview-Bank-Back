package org.hoongoin.interviewbank.interview.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InterviewPeriod {

	TWENTY_TWO1H("22년 상반기"),
	TWENTY_TWO2H("22년 하반기"),
	TWENTY_ONE1H("21년 상반기"),
	TWENTY_ONE2H("21년 하반기"),
	TWENTY_THREE1H("23년 상반기"),
	TWENTY1H("20년 상반기"),
	TWENTY2H("20년 하반기"),
	EXPECTED_INTERVIEW("예상면접"),
	ETC("기타");

	private final String koreanName;
}
