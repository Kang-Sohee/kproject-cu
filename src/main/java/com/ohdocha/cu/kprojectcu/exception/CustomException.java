package com.ohdocha.cu.kprojectcu.exception;

public class CustomException extends KnownException {

    public CustomException(int errorCode, String errorMessage) {
        super(1, errorCode, errorMessage);
    }

}
