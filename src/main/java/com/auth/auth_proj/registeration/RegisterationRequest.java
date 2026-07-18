package com.auth.auth_proj.registeration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class RegisterationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
