package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.exceptions.business.DisciplineExistException;
import com.sytoss.domain.bom.exceptions.business.GroupExistException;
import com.sytoss.domain.bom.exceptions.business.TaskDomainAlreadyExist;
import com.sytoss.domain.bom.exceptions.business.TopicExistException;
import com.sytoss.domain.bom.exceptions.business.notfound.DisciplineNotFoundException;
import com.sytoss.domain.bom.exceptions.business.notfound.TaskNotFoundException;
import com.sytoss.domain.bom.exceptions.business.notfound.TeacherNotFoundException;
import com.sytoss.domain.bom.exceptions.business.notfound.TopicNotFoundException;
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

    @ExceptionHandler({TaskNotFoundException.class})
    public ResponseEntity<?> handleValidationException(TaskNotFoundException taskNotFoundException, WebRequest request) {
        return ResponseEntity.status(404).body(taskNotFoundException.getMessage());
    }
}