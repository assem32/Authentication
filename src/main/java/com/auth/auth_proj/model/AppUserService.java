package com.auth.auth_proj.model;

import org.springframework.boot.security.autoconfigure.SecurityProperties.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final StudentRepo studentRepo;

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
        return "it worked";
    }


    
}
