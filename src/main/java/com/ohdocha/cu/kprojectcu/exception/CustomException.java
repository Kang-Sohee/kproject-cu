package com.ohdocha.cu.kprojectcu.exception;

import org.springframework.security.authentication.AuthenticationServiceException;

public class CustomException extends AuthenticationServiceException {

    public CustomException(String msg) {
        super(msg);
    }
}
