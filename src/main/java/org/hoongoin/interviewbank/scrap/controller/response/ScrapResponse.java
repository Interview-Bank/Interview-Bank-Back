package org.hoongoin.interviewbank.scrap.controller.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ScrapResponse {

	private long scrapId;
	private String title;
	private LocalDate createdAt;
}
