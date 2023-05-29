package com.sytoss.checktask.stp.controller;

import com.sytoss.checktask.stp.exceptions.DatabaseCommunicationException;
import com.sytoss.checktask.stp.exceptions.WrongEtalonException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler({DatabaseCommunicationException.class})
    @ApiResponse(responseCode = "400",
            description = "Bad request", content = @Content(mediaType = ""))
    public ResponseEntity<?> handleValidationException(DatabaseCommunicationException databaseCommunicationException) {
        return ResponseEntity.status(400).body(databaseCommunicationException.getMessage());
    }

    @ExceptionHandler({WrongEtalonException.class})
    @ApiResponse(responseCode = "406",
            description = "Not Acceptable", content = @Content(mediaType = ""))
    public ResponseEntity<?> handleValidationException(WrongEtalonException wrongEtalonException) {
        return ResponseEntity.status(406).body(wrongEtalonException.getMessage());
    }
}
