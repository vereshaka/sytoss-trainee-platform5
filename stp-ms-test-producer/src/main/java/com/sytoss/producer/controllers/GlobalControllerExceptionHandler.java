package com.sytoss.producer.controllers;

import com.sytoss.domain.bom.exceptions.business.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ExceptionHandler({PersonalExamAlreadyStartedException.class})
    public ResponseEntity<?> handleValidationException(PersonalExamAlreadyStartedException personalExamAlreadyStartedException, WebRequest request) {
        return ResponseEntity.status(409).body(personalExamAlreadyStartedException.getMessage());
    }

    @ExceptionHandler({PersonalExamIsFinishedException.class})
    public ResponseEntity<?> handleValidationException(PersonalExamIsFinishedException personalExamIsFinishedException, WebRequest request) {
        return ResponseEntity.status(409).body(personalExamIsFinishedException.getMessage());
    }

    @ExceptionHandler({AnswerInProgressException.class})
    public ResponseEntity<?> handleValidationException(AnswerInProgressException answerInProgressException, WebRequest request) {
        return ResponseEntity.status(409).body(answerInProgressException.getMessage());
    }

    @ExceptionHandler({AnswerIsAnsweredException.class})
    public ResponseEntity<?> handleValidationException(AnswerIsAnsweredException answerIsAnsweredException, WebRequest request) {
        return ResponseEntity.status(409).body(answerIsAnsweredException.getMessage());
    }

    @ExceptionHandler({PersonalExamHasNoAnswerException.class})
    public ResponseEntity<?> handleValidationException(PersonalExamHasNoAnswerException personalExamHasNoAnswerException, WebRequest request) {
        return ResponseEntity.status(404).body(personalExamHasNoAnswerException.getMessage());
    }
}