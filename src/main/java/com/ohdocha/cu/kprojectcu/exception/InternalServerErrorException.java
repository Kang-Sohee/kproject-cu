package com.ohdocha.cu.kprojectcu.exception;

public class InternalServerErrorException extends KnownException {

    public InternalServerErrorException(int errorCode, String errorMessage) {
        super(500, errorCode, errorMessage);
    }

}
