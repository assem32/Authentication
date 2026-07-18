package com.auth.auth_proj.registeration.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenService {

    @Autowired
    private ConfirmationTokenRepo confirmationTokenRepo;

    public void saveConfirmationToken(ConfirmationToken confirmationToken){
        confirmationTokenRepo.save(confirmationToken);
    }
    
}
