package com.sytoss.checktask.stp.controller;

import com.sytoss.checktask.stp.exceptions.DatabaseCommunicationException;
import com.sytoss.checktask.stp.exceptions.WrongEtalonException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler({DatabaseCommunicationException.class})
    public ResponseEntity<?> handleValidationException(DatabaseCommunicationException databaseCommunicationException) {
        return ResponseEntity.status(400).body(databaseCommunicationException.getMessage());
    }

    @ExceptionHandler({WrongEtalonException.class})
    public ResponseEntity<?> handleValidationException(WrongEtalonException wrongEtalonException) {
        return ResponseEntity.status(406).body(wrongEtalonException.getMessage());
    }
}
