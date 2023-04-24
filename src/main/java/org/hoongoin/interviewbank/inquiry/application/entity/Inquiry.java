package org.hoongoin.interviewbank.inquiry.application.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hoongoin.interviewbank.exception.IbValidationException;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
public class Inquiry {

	@Builder
	public Inquiry(String email, String title, String content, String attachedFileUrl, Boolean isAnswered) {
		this.setEmail(email);
		this.setTitle(title);
		this.setContent(content);
		this.setAttachedFileUrl(attachedFileUrl);
		this.setIsAnswered(isAnswered);
	}

	private String email;
	private String title;
	private String content;
	private String attachedFileUrl;
	private Boolean isAnswered;

	private void setEmail(String email) {
		if (email == null || email.isEmpty()) {
			log.info("email null");
			throw new IbValidationException("Email is required");
		}
		String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(email);
		if (!m.matches()) {
			log.info("Email Validation Exception");
			throw new IbValidationException("Email Validation Exception");
		}
		if (email.length() > 120) {
			log.info("email over length of 120");
			throw new IbValidationException("Email over length of 120");
		}
		this.email = email;
	}

	private void setTitle(String title) {
		if (title == null || title.isEmpty()) {
			log.info("title null");
			throw new IbValidationException("Title is required");
		}
		if (title.length() > 128) {
			log.info("title over length of 128");
			throw new IbValidationException("Title over length of 128");
		}
		this.title = title;
	}

	private void setContent(String content) {
		if (content == null || content.isEmpty()) {
			log.info("content null");
			throw new IbValidationException("Content is required");
		}
		if (content.length() > 100000) {
			log.info("content over length of 100000");
			throw new IbValidationException("Content over length of 100000");
		}
		this.content = content;
	}
}
