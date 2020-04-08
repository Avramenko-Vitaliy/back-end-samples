package com.sample.spring.security.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Credentials {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
