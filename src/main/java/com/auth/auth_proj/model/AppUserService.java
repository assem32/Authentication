package com.auth.auth_proj.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.auth_proj.registeration.token.ConfirmationToken;
import com.auth.auth_proj.registeration.token.ConfirmationTokenService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final StudentRepo studentRepo;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    private BCryptPasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return studentRepo.findByEmail(email).orElseThrow(
            ()-> new UsernameNotFoundException("mail " + email +" not found")
        );
    }

    public String signUpUser(Users user){
        boolean isEmailExist = studentRepo.findByEmail(user.getEmail()).isPresent();
        if(isEmailExist){
            throw new IllegalStateException("this mail already exists");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        studentRepo.save(user);

        // TODO: conformation

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
            token,
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15),
            user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        // TODO send emai

        return token;
    }

    public int enableAppUser(String email) {
        return studentRepo.enableAppUser(email);
    }


    
}
