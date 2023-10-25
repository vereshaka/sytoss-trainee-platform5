package com.sytoss.producer.controllers;

import com.sytoss.domain.bom.exceptions.ApplicationError;
import com.sytoss.domain.bom.exceptions.business.*;
import com.sytoss.domain.bom.exceptions.business.notfound.ExamNotFoundException;
import com.sytoss.domain.bom.exceptions.business.notfound.PersonalExamNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ExceptionHandler({PersonalExamAlreadyStartedException.class})
    public ResponseEntity<ApplicationError> handleValidationException(PersonalExamAlreadyStartedException personalExamAlreadyStartedException) {
        return ResponseEntity.status(409).body(new ApplicationError(personalExamAlreadyStartedException));
    }

    @ExceptionHandler({PersonalExamIsFinishedException.class})
    public ResponseEntity<ApplicationError> handleValidationException(PersonalExamIsFinishedException personalExamIsFinishedException) {
        return ResponseEntity.status(409).body(new ApplicationError(personalExamIsFinishedException));
    }

    @ExceptionHandler({AnswerInProgressException.class})
    public ResponseEntity<ApplicationError> handleValidationException(AnswerInProgressException answerInProgressException) {
        return ResponseEntity.status(409).body(new ApplicationError(answerInProgressException));
    }

    @ExceptionHandler({AnswerIsAnsweredException.class})
    public ResponseEntity<ApplicationError> handleValidationException(AnswerIsAnsweredException answerIsAnsweredException) {
        return ResponseEntity.status(409).body(new ApplicationError(answerIsAnsweredException));
    }

    @ExceptionHandler({PersonalExamHasNoAnswerException.class})
    public ResponseEntity<ApplicationError> handleValidationException(PersonalExamHasNoAnswerException personalExamHasNoAnswerException) {
        return ResponseEntity.status(404).body(new ApplicationError(personalExamHasNoAnswerException));
    }

    @ExceptionHandler({PersonalExamNotFoundException.class})
    public ResponseEntity<ApplicationError> handleValidationException(PersonalExamNotFoundException personalExamNotFoundException) {
        return ResponseEntity.status(404).body(new ApplicationError(personalExamNotFoundException));
    }

    @ExceptionHandler({RequestIsNotValidException.class})
    public ResponseEntity<ApplicationError> handleInvalidRequesException(RequestIsNotValidException invalidRequestException) {
        return ResponseEntity.status(400).body(new ApplicationError(invalidRequestException));
    }

    @ExceptionHandler({ExamNotFoundException.class})
    public ResponseEntity<ApplicationError> handleValidationException(ExamNotFoundException examNotFoundException) {
        return ResponseEntity.status(404).body(new ApplicationError(examNotFoundException));
    }

    @ExceptionHandler({TaskCountNotValidException.class})
    public ResponseEntity<ApplicationError> handleValidationException(TaskCountNotValidException taskCountNotValidException) {
        return ResponseEntity.status(409).body(new ApplicationError(taskCountNotValidException));
    }
}