package org.hoongoin.interviewbank.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class MailUtil {

	private final JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String email;

	@Async("mail")
	public void sendMailTo(String toEmail, String subject, String text) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(email);
		simpleMailMessage.setTo(toEmail);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(text);
		javaMailSender.send(simpleMailMessage);
	}
}
