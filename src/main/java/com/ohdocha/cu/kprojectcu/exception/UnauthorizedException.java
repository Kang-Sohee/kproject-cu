package com.ohdocha.cu.kprojectcu.exception;

public class UnauthorizedException extends KnownException {

    public UnauthorizedException(int errorCode, String errorMessage) {
        super(401, errorCode, errorMessage);
    }

}
