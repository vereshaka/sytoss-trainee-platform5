package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.exceptions.businessException.*;
import com.sytoss.domain.bom.exceptions.businessException.notFound.DisciplineNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ExceptionHandler({DisciplineNotFoundException.class})
    public ResponseEntity<?> handleValidationException(DisciplineNotFoundException disciplineNotFoundException, WebRequest request) {
        return ResponseEntity.status(404).body(disciplineNotFoundException.getMessage());
    }
}