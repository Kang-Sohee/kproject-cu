package com.ohdocha.cu.kprojectcu.exception;

public class NotFoundException extends KnownException {

    public NotFoundException(String errorMessage) {
        super(404, 9999, errorMessage);
    }

    public NotFoundException(int errorCode, String errorMessage) {
        super(404, errorCode, errorMessage);
    }

}
