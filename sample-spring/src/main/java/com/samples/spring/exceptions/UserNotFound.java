package com.samples.spring.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotFound extends ResponseStatusException {

    public UserNotFound(String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }
}
