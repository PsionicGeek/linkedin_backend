package org.psionicgeek.linkedin.userservice.dto;

import lombok.Data;

@Data
public class SignupRequestDto {
    private String name;


    private String email;


    private String password;
}
