package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.exceptions.ApplicationError;
import com.sytoss.domain.bom.exceptions.business.*;
import com.sytoss.domain.bom.exceptions.business.notfound.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ExceptionHandler({TopicExistException.class})
    public ResponseEntity<ApplicationError> handleValidationException(TopicExistException topicExistException) {
        return ResponseEntity.status(409).body(new ApplicationError(topicExistException));
    }

    @ExceptionHandler({DisciplineNotFoundException.class})
    public ResponseEntity<ApplicationError> handleValidationException(DisciplineNotFoundException disciplineNotFoundException) {
        return ResponseEntity.status(404).body(new ApplicationError(disciplineNotFoundException));
    }

    @ExceptionHandler({DisciplineExistException.class})
    public ResponseEntity<ApplicationError> handleValidationException(DisciplineExistException disciplineExistException) {
        return ResponseEntity.status(409).body(new ApplicationError(disciplineExistException));
    }

    @ExceptionHandler({TeacherNotFoundException.class})
    public ResponseEntity<ApplicationError> handleValidationException(TeacherNotFoundException teacherNotFoundException) {
        return ResponseEntity.status(404).body(new ApplicationError(teacherNotFoundException));
    }

    @ExceptionHandler({TaskNotFoundException.class})
    public ResponseEntity<ApplicationError> handleValidationException(TaskNotFoundException taskNotFoundException) {
        return ResponseEntity.status(404).body(new ApplicationError(taskNotFoundException));
    }

    @ExceptionHandler({TopicNotFoundException.class})
    public ResponseEntity<ApplicationError> handleValidationException(TopicNotFoundException topicNotFoundException) {
        return ResponseEntity.status(404).body(new ApplicationError(topicNotFoundException));
    }

    @ExceptionHandler({TaskDomainAlreadyExist.class})
    public ResponseEntity<ApplicationError> handleValidationException(TaskDomainAlreadyExist taskDomainAlreadyExist) {
        return ResponseEntity.status(409).body(new ApplicationError(taskDomainAlreadyExist));
    }

    @ExceptionHandler({GroupExistException.class})
    public ResponseEntity<ApplicationError> handleValidationException(GroupExistException groupExistException) {
        return ResponseEntity.status(409).body(new ApplicationError(groupExistException));
    }

    @ExceptionHandler({TaskDomainNotFoundException.class})
    public ResponseEntity<ApplicationError> handleValidationException(TaskDomainNotFoundException taskDomainNotFoundException) {
        return ResponseEntity.status(404).body(new ApplicationError(taskDomainNotFoundException));
    }

    @ExceptionHandler({TaskExistException.class})
    public ResponseEntity<ApplicationError> handleValidationException(TaskExistException taskExistException) {
        return ResponseEntity.status(409).body(new ApplicationError(taskExistException));
    }

    @ExceptionHandler({TaskConditionNotFoundException.class})
    public ResponseEntity<ApplicationError> handleValidationException(TaskConditionNotFoundException taskConditionNotFound) {
        return ResponseEntity.status(404).body(new ApplicationError(taskConditionNotFound));
    }

    @ExceptionHandler({TaskDontHasConditionException.class})
    public ResponseEntity<ApplicationError> handleValidationException(TaskDontHasConditionException taskDontHaveConditionException) {
        return ResponseEntity.status(404).body(new ApplicationError(taskDontHaveConditionException));
    }

    @ExceptionHandler({TaskDomainCouldNotCreateImageException.class})
    public ResponseEntity<ApplicationError> handleValidationException(TaskDomainCouldNotCreateImageException taskDomainCouldNotCreateImageException) {
        return ResponseEntity.status(409).body(new ApplicationError(taskDomainCouldNotCreateImageException));
    }

    @ExceptionHandler({TaskConditionAlreadyExistException.class})
    public ResponseEntity<ApplicationError> handleValidationException(TaskConditionAlreadyExistException taskConditionAlreadyExistException) {
        return ResponseEntity.status(409).body(new ApplicationError(taskConditionAlreadyExistException));
    }

    @ExceptionHandler({TaskDomainIsUsed.class})
    public ResponseEntity<ApplicationError> handleValidationException(TaskDomainIsUsed taskDomainIsUsed) {
        return ResponseEntity.status(409).body(new ApplicationError(taskDomainIsUsed));
    }

    @ExceptionHandler({EtalonIsNotValidException.class})
    public ResponseEntity<ApplicationError> handleValidationException(EtalonIsNotValidException etalonIsNotValidException) {
        return ResponseEntity.status(409).body(new ApplicationError(etalonIsNotValidException));
    }

    @ExceptionHandler({RequestIsNotValidException.class})
    public ResponseEntity<ApplicationError> handleInvalidRequesException(RequestIsNotValidException invalidRequestException) {
        return ResponseEntity.status(400).body(new ApplicationError(invalidRequestException));
    }

    @ExceptionHandler({UserNotIdentifiedException.class})
    public ResponseEntity<ApplicationError> handleValidationException(UserNotIdentifiedException userNotIdentifiedException) {
        return ResponseEntity.status(403).body(new ApplicationError(userNotIdentifiedException));
    }

    @ExceptionHandler({ExamNotFoundException.class})
    public ResponseEntity<ApplicationError> handleValidationException(ExamNotFoundException examNotFoundException) {
        return ResponseEntity.status(404).body(new ApplicationError(examNotFoundException));
    }
}