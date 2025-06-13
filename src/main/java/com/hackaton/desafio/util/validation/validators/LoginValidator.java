package com.hackaton.desafio.util.validation.validators;

import com.hackaton.desafio.dto.authDTO.LoginRequest;
import com.hackaton.desafio.util.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class LoginValidator implements Validator<LoginRequest> {

    @Override
    public void validate(LoginRequest input) {
        if (input.name() == null || input.name().isBlank()) {
            throw new IllegalArgumentException("Invalid Credentials");
        }
        if (input.password() == null || input.password().isBlank()) {
            throw new IllegalArgumentException("Invalid Credentials");
        }
    }
}
