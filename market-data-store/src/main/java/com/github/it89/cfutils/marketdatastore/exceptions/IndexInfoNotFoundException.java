package com.github.it89.cfutils.marketdatastore.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class IndexInfoNotFoundException extends RuntimeException {
}
