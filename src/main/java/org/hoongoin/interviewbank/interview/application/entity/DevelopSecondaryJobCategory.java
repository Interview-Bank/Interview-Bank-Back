package org.hoongoin.interviewbank.interview.application.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DevelopSecondaryJobCategory {

	BACKEND("백엔드"),
	FRONTEND("프런트엔드"),
	ANDROID("안드로이드"),
	IOS("IOS"),
	MOBILE("모바일"),
	DEVOPS("DevOps"),
	QA("QA"),
	GAME("게임"),
	AI("AI");

	private final String developSecondaryJobName;
}
