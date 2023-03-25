package org.hoongoin.interviewbank.inquiry.application.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Inquiry {

	@Builder
	public Inquiry(String email, String title, String content, String attachedFileUrl) {
		this.setEmail(email);
		this.setTitle(title);
		this.setContent(content);
		this.setAttachedFileUrl(attachedFileUrl);
	}

	private String email;
	private String title;
	private String content;
	private String attachedFileUrl;
}
