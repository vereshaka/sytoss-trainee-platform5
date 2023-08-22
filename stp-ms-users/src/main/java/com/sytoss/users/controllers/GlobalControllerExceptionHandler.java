package com.sytoss.users.controllers;

import com.sytoss.domain.bom.exceptions.ApplicationError;
import com.sytoss.domain.bom.exceptions.business.GroupExistException;
import com.sytoss.domain.bom.exceptions.business.NotAllowedTeacherRegistrationException;
import com.sytoss.domain.bom.exceptions.business.notfound.GroupNotFoundException;
import com.sytoss.users.services.exceptions.UserNotFoundException;
import com.sytoss.users.services.exceptions.UserPhotoNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ExceptionHandler({GroupExistException.class})
    public ResponseEntity<?> handleValidationException(GroupExistException groupExistException) {
        return ResponseEntity.status(409).body(new ApplicationError(groupExistException));
    }

    @ExceptionHandler({GroupNotFoundException.class})
    public ResponseEntity<?> handleValidationException(GroupNotFoundException groupNotFoundException) {
        return ResponseEntity.status(404).body(new ApplicationError(groupNotFoundException));
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<?> handleValidationException(UserNotFoundException userNotFoundException) {
        return ResponseEntity.status(404).body(new ApplicationError(userNotFoundException));
    }

    @ExceptionHandler({UserPhotoNotFoundException.class})
    public ResponseEntity<?> handleValidationException(UserPhotoNotFoundException userPhotoNotFoundException) {
        return ResponseEntity.status(404).body(new ApplicationError(userPhotoNotFoundException));
    }

    @ExceptionHandler({NotAllowedTeacherRegistrationException.class})
    public ResponseEntity<?> handleValidationException(NotAllowedTeacherRegistrationException notAllowedTeacherRegistrationException) {
        return ResponseEntity.status(403).body(new ApplicationError(notAllowedTeacherRegistrationException));
    }
}