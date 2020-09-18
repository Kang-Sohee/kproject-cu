package com.ohdocha.cu.kprojectcu.exception;

public class BadRequestException extends KnownException {

    public BadRequestException(int errorCode, String errorMessage) {
        super(400, errorCode, errorMessage);
    }

}
