package com.sytoss.producer.controllers;

import com.sytoss.domain.bom.exceptions.ApplicationError;
import com.sytoss.domain.bom.exceptions.business.*;
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
}