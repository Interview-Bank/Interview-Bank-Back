package org.hoongoin.interviewbank.account.application;

import java.util.Objects;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.controller.request.LoginRequest;
import org.hoongoin.interviewbank.account.controller.request.RegisterRequest;
import org.hoongoin.interviewbank.account.controller.request.ResetPasswordRequest;
import org.hoongoin.interviewbank.account.controller.request.SendEmailRequest;
import org.hoongoin.interviewbank.account.controller.response.RegisterResponse;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.account.domain.AccountCommandService;
import org.hoongoin.interviewbank.account.domain.AccountQueryService;
import org.hoongoin.interviewbank.account.domain.PasswordResetTokenCommand;
import org.hoongoin.interviewbank.account.domain.PasswordResetTokenQuery;
import org.hoongoin.interviewbank.account.infrastructure.entity.AccountType;
import org.hoongoin.interviewbank.account.infrastructure.entity.PasswordResetToken;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.exception.IbLoginFailedException;
import org.hoongoin.interviewbank.exception.IbValidationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountService {

	private final AccountCommandService accountCommandService;
	private final AccountQueryService accountQueryService;
	private final AccountMapper accountMapper;
	private final BCryptPasswordEncoder passwordEncoder;
	private final PasswordResetTokenProvider passwordResetTokenProvider;
	private final PasswordResetTokenCommand passwordResetTokenCommand;
	private final PasswordResetTokenQuery passwordResetTokenQuery;
	private final MailService mailService;

	public RegisterResponse registerByRegisterRequest(RegisterRequest registerRequest) {
		Account account = accountMapper.registerRequestToAccount(registerRequest);
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		account.setAccountType(AccountType.EMAIL);
		account = accountCommandService.insertAccount(account);
		return accountMapper.accountToRegisterResponse(account);
	}

	public Account loginByLoginRequest(LoginRequest loginRequest) {
		Account account;
		try {
			account = accountQueryService.findAccountByEmail(loginRequest.getEmail());
		} catch (IbEntityNotFoundException e) {
			throw new IbLoginFailedException("Email or Password is not correct");
		}

		if (!passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
			throw new IbLoginFailedException("Email or Password is not correct");
		}

		return account;
	}

	public void createPasswordResetTokenAndSendEmailByRequest(SendEmailRequest sendEmailRequest) {
		Account account = accountQueryService.findAccountByEmail(sendEmailRequest.getEmail());

		String hashedToken = passwordResetTokenProvider.createToken();
		passwordResetTokenCommand.saveToken(account.getAccountId(), sendEmailRequest.getEmail(), hashedToken);

		mailService.sendMailTo(sendEmailRequest.getEmail(), hashedToken);
	}

	public boolean validateToken(String token) {
		return passwordResetTokenQuery.existsPasswordResetTokenByHashedToken(token);
	}

	@Transactional
	public void resetPasswordByTokenAndRequest(String token, ResetPasswordRequest resetPasswordRequest) {
		PasswordResetToken passwordResetToken = passwordResetTokenQuery.findResetTokenByToken(token);
		long requestingAccountId = passwordResetToken.getAccountId();

		if (!Objects.equals(resetPasswordRequest.getNewPassword(), resetPasswordRequest.getNewPasswordCheck())) {
			throw new IbValidationException("Password and Password Check is not same");
		}

		String encodedPassword = passwordEncoder.encode(resetPasswordRequest.getNewPasswordCheck());
		accountCommandService.resetPassword(requestingAccountId, encodedPassword);
	}
}
