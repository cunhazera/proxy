package com.shortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ShortedURLNotFoundException extends RuntimeException {

    public ShortedURLNotFoundException(String code) {
        super(String.format("The code %s doesn't exist", code));
    }

}
