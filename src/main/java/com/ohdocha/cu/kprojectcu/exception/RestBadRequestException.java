package com.ohdocha.cu.kprojectcu.exception;

public class RestBadRequestException extends RestKnownException {

    public RestBadRequestException(String errorMessage) {
        super(400, 9999, errorMessage);
    }

    public RestBadRequestException(int errorCode, String errorMessage) {
        super(400, errorCode, errorMessage);
    }

}
