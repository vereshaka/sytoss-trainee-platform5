package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.exceptions.businessException.DisciplineExistException;
import com.sytoss.domain.bom.exceptions.businessException.DisciplineNotFoundException;
import com.sytoss.domain.bom.exceptions.businessException.TeacherNotFoundException;
import com.sytoss.domain.bom.exceptions.businessException.TopicExistException;
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
        return ResponseEntity.status(409).body(disciplineNotFoundException.getMessage());
    }

    @ExceptionHandler({DisciplineExistException.class})
    public ResponseEntity<?> handleValidationException(DisciplineExistException disciplineExistException, WebRequest request) {
        return ResponseEntity.status(409).body(disciplineExistException.getMessage());
    }

    @ExceptionHandler({TeacherNotFoundException.class})
    public ResponseEntity<?> handleValidationException(TeacherNotFoundException teacherNotFoundException, WebRequest request) {
        return ResponseEntity.status(404).body(teacherNotFoundException.getMessage());
    }

}