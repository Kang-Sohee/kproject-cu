package com.ohdocha.cu.kprojectcu.exception;

public class RestForbiddenException extends RestKnownException {

    public RestForbiddenException(String errorMessage) {
        super(403, 9999, errorMessage);
    }

    public RestForbiddenException(int errorCode, String errorMessage) {
        super(403, errorCode, errorMessage);
    }

}
