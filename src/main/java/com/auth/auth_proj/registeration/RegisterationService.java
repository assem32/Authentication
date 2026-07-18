package com.auth.auth_proj.registeration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth.auth_proj.model.AppUserRole;
import com.auth.auth_proj.model.AppUserService;
import com.auth.auth_proj.model.Users;

@Service
public class RegisterationService {

	@Autowired
	private EmailValidator emailValidator;

	@Autowired
	private AppUserService appUserService;

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
    
}
