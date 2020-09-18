package com.ohdocha.cu.kprojectcu.exception;

public class RestInternalServerErrorException extends RestKnownException {

    public RestInternalServerErrorException(String errorMessage) {
        super(500, 9999, errorMessage);
    }

    public RestInternalServerErrorException(int errorCode, String errorMessage) {
        super(500, errorCode, errorMessage);
    }

}
