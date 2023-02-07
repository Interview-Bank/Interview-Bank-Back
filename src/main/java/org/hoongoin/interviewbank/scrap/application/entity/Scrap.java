package org.hoongoin.interviewbank.scrap.application.entity;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.hoongoin.interviewbank.exception.IbValidationException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Scrap {

	private long scrapId;
	private long accountId;
	private long interviewId;
	private String title;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public void setTitle(String title) {
		if (title.getBytes(StandardCharsets.UTF_8).length > 128) {
			throw new IbValidationException("Title over 128 byte");
		}
	}
}
