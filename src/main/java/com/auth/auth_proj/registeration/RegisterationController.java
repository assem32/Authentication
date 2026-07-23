package com.auth.auth_proj.registeration;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("api/v1/registration")
public class RegisterationController {

    @Autowired
    private RegisterationService registerationService;

    @PostMapping
    public String register(@RequestBody RegisterationRequest registerationRequest){
        return registerationService.register(registerationRequest);
    }

    @GetMapping("/confirm")
    public String getMethodName(@RequestParam String token) {
        return registerationService.confirmToken(token);
    }
    
    
    
}
