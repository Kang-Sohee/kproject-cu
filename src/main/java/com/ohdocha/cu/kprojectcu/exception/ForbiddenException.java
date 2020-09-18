package com.ohdocha.cu.kprojectcu.exception;

public class ForbiddenException extends KnownException {

    public ForbiddenException(String errorMessage) {
        super(403, 9999, errorMessage);
    }

    public ForbiddenException(int errorCode, String errorMessage) {
        super(403, errorCode, errorMessage);
    }

}
