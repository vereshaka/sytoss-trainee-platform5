package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.exceptions.business.*;
import com.sytoss.domain.bom.exceptions.business.notfound.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ExceptionHandler({TopicExistException.class})
    public ResponseEntity<?> handleValidationException(TopicExistException topicExistException, WebRequest request) {
        return ResponseEntity.status(409).body(topicExistException.getMessage());
    }

    @ExceptionHandler({DisciplineNotFoundException.class})
    public ResponseEntity<?> handleValidationException(DisciplineNotFoundException disciplineNotFoundException, WebRequest request) {
        return ResponseEntity.status(404).body(disciplineNotFoundException.getMessage());
    }

    @ExceptionHandler({DisciplineExistException.class})
    public ResponseEntity<?> handleValidationException(DisciplineExistException disciplineExistException, WebRequest request) {
        return ResponseEntity.status(409).body(disciplineExistException.getMessage());
    }

    @ExceptionHandler({TeacherNotFoundException.class})
    public ResponseEntity<?> handleValidationException(TeacherNotFoundException teacherNotFoundException, WebRequest request) {
        return ResponseEntity.status(404).body(teacherNotFoundException.getMessage());
    }

    @ExceptionHandler({TaskNotFoundException.class})
    public ResponseEntity<?> handleValidationException(TaskNotFoundException taskNotFoundException, WebRequest request) {
        return ResponseEntity.status(404).body(taskNotFoundException.getMessage());
    }

    @ExceptionHandler({TopicNotFoundException.class})
    public ResponseEntity<?> handleValidationException(TopicNotFoundException topicNotFoundException, WebRequest request) {
        return ResponseEntity.status(404).body(topicNotFoundException.getMessage());
    }

    @ExceptionHandler({TaskDomainAlreadyExist.class})
    public ResponseEntity<?> handleValidationException(TaskDomainAlreadyExist taskDomainAlreadyExist, WebRequest request) {
        return ResponseEntity.status(409).body(taskDomainAlreadyExist.getMessage());
    }

    @ExceptionHandler({GroupExistException.class})
    public ResponseEntity<?> handleValidationException(GroupExistException groupExistException, WebRequest request) {
        return ResponseEntity.status(409).body(groupExistException.getMessage());
    }

    @ExceptionHandler({TaskDomainNotFoundException.class})
    public ResponseEntity<?> handleValidationException(TaskDomainNotFoundException taskDomainNotFoundException, WebRequest request) {
        return ResponseEntity.status(404).body(taskDomainNotFoundException.getMessage());
    }

    @ExceptionHandler({TaskExistException.class})
    public ResponseEntity<?> handleValidationException(TaskExistException taskExistException, WebRequest request) {
        return ResponseEntity.status(409).body(taskExistException.getMessage());
    }

    @ExceptionHandler({TaskDomainIsUsed.class})
    public ResponseEntity<?> handleValidationException(TaskDomainIsUsed taskDomainIsUsed, WebRequest request) {
        return ResponseEntity.status(409).body(taskDomainIsUsed.getMessage());
    }
}