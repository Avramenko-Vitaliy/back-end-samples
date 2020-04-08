package com.samples.spring.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@CommonsLog
@RestController
@ControllerAdvice
@AllArgsConstructor
public class ErrorHandlingController extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> responseStatusException(ResponseStatusException ex) {
        log.debug(ex.getMessage(), ex);
        return new ResponseEntity<>(String.format("{ \"message\": \"%s\" }", ex.getMessage()), ex.getStatus());
    }
}
