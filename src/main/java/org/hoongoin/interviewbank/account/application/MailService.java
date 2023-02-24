package org.hoongoin.interviewbank.account.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {

	private final JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String email;

	@Async
	public void sendMailTo(String toEmail, String hashedToken) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(email);
		simpleMailMessage.setTo(toEmail);
		simpleMailMessage.setSubject("[Interview Bank] 비밀번호 재설정 안내");
		simpleMailMessage.setText(hashedToken);
		javaMailSender.send(simpleMailMessage);
	}
}
