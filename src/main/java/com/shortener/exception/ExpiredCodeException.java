package com.shortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.GONE)
public class ExpiredCodeException extends RuntimeException {

    public ExpiredCodeException(String code) {
        super(String.format("The code %s is expired", code));
    }
}
