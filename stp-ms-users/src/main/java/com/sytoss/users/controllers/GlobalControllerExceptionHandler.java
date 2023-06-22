package com.sytoss.users.controllers;

import com.sytoss.domain.bom.exceptions.business.GroupExistException;
import com.sytoss.domain.bom.exceptions.business.notfound.GroupNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ExceptionHandler({GroupExistException.class})
    public ResponseEntity<?> handleValidationException(GroupExistException groupExistException, WebRequest request) {
        return ResponseEntity.status(409).body(groupExistException.getMessage());
    }

    @ExceptionHandler({GroupNotFoundException.class})
    public ResponseEntity<?> handleValidationException(GroupNotFoundException groupNotFoundException, WebRequest request) {
        return ResponseEntity.status(404).body(groupNotFoundException.getMessage());
    }

}