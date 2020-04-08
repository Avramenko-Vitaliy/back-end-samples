package com.sample.spring.security.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;
import java.util.Optional;

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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String responseStatusException(EntityNotFoundException ex) {
        log.debug(ex.getMessage(), ex);
        return String.format("{ \"message\": \"%s\" }", ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.debug(ex.getMessage());
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String message = ex.getMessage();
        if (Objects.isNull(fieldError) || Objects.isNull(fieldError.getDefaultMessage())) {
            message = Optional.ofNullable(ex.getBindingResult().getGlobalError())
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .orElse(message);
            return new ResponseEntity<>(String.format("{ \"message\": \"%s\" }", message), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        switch (fieldError.getDefaultMessage()) {
            case "must not be null":
            case "must not be blank":
            case "must not be empty":
                message = String.format("Field '%s' is required", fieldError.getField());
                break;
            default:
                message = fieldError.getDefaultMessage();
                break;
        }
        return new ResponseEntity<>(String.format("{ \"message\": \"%s\" }", message), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
