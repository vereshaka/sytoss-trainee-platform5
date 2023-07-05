package com.sytoss.provider.controller;

import com.sytoss.domain.bom.exceptions.ApplicationError;
import com.sytoss.provider.exceptions.ConvertToImageException;
import com.sytoss.provider.exceptions.ImageNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ExceptionHandler({ConvertToImageException.class})
    public ResponseEntity<?> handleValidationException(ConvertToImageException convertToImageException) {
        return ResponseEntity.status(400).body(new ApplicationError(convertToImageException));
    }

    @ExceptionHandler({ImageNotFoundException.class})
    public ResponseEntity<?> handleValidationException(ImageNotFoundException imageNotFoundException) {
        return ResponseEntity.status(404).body(new ApplicationError(imageNotFoundException));
    }
}