package com.sytoss.users.controllers;

import com.sytoss.domain.bom.exceptions.business.GroupExistException;
import com.sytoss.domain.bom.exceptions.business.notfound.GroupNotFoundException;
import com.sytoss.users.services.exceptions.UserNotFoundException;
import com.sytoss.users.services.exceptions.UserPhotoNotFoundException;
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

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<?> handleValidationException(UserNotFoundException userNotFoundException, WebRequest request) {
        return ResponseEntity.status(404).body(userNotFoundException.getMessage());
    }

    @ExceptionHandler({UserPhotoNotFoundException.class})
    public ResponseEntity<?> handleValidationException(UserPhotoNotFoundException userPhotoNotFoundException, WebRequest request) {
        return ResponseEntity.status(404).body(userPhotoNotFoundException.getMessage());
    }
}