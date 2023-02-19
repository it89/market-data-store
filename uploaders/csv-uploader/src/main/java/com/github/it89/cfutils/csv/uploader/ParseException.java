package com.github.it89.cfutils.csv.uploader;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ParseException extends RuntimeException {
    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
