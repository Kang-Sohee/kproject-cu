package com.ohdocha.cu.kprojectcu.exception;

import com.google.gson.Gson;
import lombok.Getter;

@Getter
public class KnownException extends RuntimeException {

    protected int httpStatusCode;
    protected int errorCode;
    protected String errorMessage;

    public KnownException(int httpStatusCode, int errorCode, String errorMessage) {
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
