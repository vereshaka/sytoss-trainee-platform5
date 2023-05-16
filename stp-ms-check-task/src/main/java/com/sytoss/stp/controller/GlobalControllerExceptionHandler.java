package com.sytoss.stp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleValidationException(Exception exception, WebRequest request) {
        return ResponseEntity.status(404).body(exception.getMessage());
    }
}