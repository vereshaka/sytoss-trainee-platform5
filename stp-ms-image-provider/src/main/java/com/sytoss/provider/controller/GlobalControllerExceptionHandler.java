package com.sytoss.provider.controller;

import com.sytoss.provider.exceptions.ConvertToImageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ExceptionHandler({ConvertToImageException.class})
    public ResponseEntity<?> handleValidationException(ConvertToImageException convertToImageException, WebRequest request) {
        return ResponseEntity.status(400).body(convertToImageException.getMessage());
    }
}