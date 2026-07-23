package com.auth.auth_proj.registeration;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth.auth_proj.model.AppUserRole;
import com.auth.auth_proj.model.AppUserService;
import com.auth.auth_proj.model.Users;
import com.auth.auth_proj.registeration.token.ConfirmationToken;
import com.auth.auth_proj.registeration.token.ConfirmationTokenService;

import jakarta.transaction.Transactional;

@Service
public class RegisterationService {

	@Autowired
	private EmailValidator emailValidator;

	@Autowired
	private AppUserService appUserService;

	@Autowired
	private ConfirmationTokenService confirmationTokenService;

	public String register(RegisterationRequest registerationRequest) {
		boolean isEmailValid = emailValidator.test(registerationRequest.getEmail());
		if (!isEmailValid) {
			throw new IllegalStateException("email not valid");
		}
		return appUserService.signUpUser(new Users(
				registerationRequest.getFirstName() + " " + registerationRequest.getLastName(),
				registerationRequest.getEmail(),
				registerationRequest.getPassword(),
				AppUserRole.USER));
	}

	@Transactional
	public String confirmToken(String token){
		ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(
		()-> new IllegalStateException("token not found")
		);

		if(confirmationToken.getConfirmedAt() !=null){
			throw new IllegalStateException("Email already confirmed");
		}

		LocalDateTime expireTime = confirmationToken.getExpiredAt();

		if(expireTime.isBefore(LocalDateTime.now())){
			throw new IllegalStateException("Token Expired");
		}

		confirmationTokenService.setConfirmedAt(token);
		appUserService.enableAppUser(confirmationToken.getUser().getEmail());

		return "confirmed";
	}
    
}
