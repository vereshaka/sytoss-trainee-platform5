package com.sytoss.checktask.stp.controller;

import com.sytoss.checktask.stp.exceptions.RequestIsNotValidException;
import com.sytoss.checktask.stp.exceptions.DatabaseCommunicationException;
import com.sytoss.checktask.stp.exceptions.WrongEtalonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ExceptionHandler({DatabaseCommunicationException.class})
    public ResponseEntity<?> handleValidationException(DatabaseCommunicationException databaseCommunicationException) {
        log.error("Global Exception Handler", databaseCommunicationException);
        return ResponseEntity.status(400).body(databaseCommunicationException.getMessage());
    }

    @ExceptionHandler({WrongEtalonException.class})
    public ResponseEntity<?> handleValidationException(WrongEtalonException wrongEtalonException) {
        log.error("Global Exception Handler", wrongEtalonException);
        return ResponseEntity.status(406).body(wrongEtalonException.getMessage());
    }

    @ExceptionHandler({RequestIsNotValidException.class})
    public ResponseEntity<?> handleValidationException(RequestIsNotValidException requestIsNotValidException) {
        log.error("Global Exception Handler", requestIsNotValidException);
        return ResponseEntity.status(406).body(requestIsNotValidException.getMessage());
    }
}
