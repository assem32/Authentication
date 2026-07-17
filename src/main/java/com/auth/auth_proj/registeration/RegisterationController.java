package com.auth.auth_proj.registeration;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/v1/registeration")
@AllArgsConstructor
public class RegisterationController {

    private RegisterationService registerationService;

    public String register(@RequestBody RegisterationRequest registerationRequest){
        return registerationService.register(registerationRequest);
    }
    
    
}
