package com.sytoss.traineeplatform.controllers;

import com.sytoss.traineeplatform.bom.Task;
import com.sytoss.traineeplatform.requestbodies.AnswerRequestBody;
import com.sytoss.traineeplatform.services.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class ExamController {

    private final AnswerService answerService;

    @Autowired
    public ExamController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping("/{id}/task/{number}/answer")
    public ResponseEntity<Task> answerRequest(@PathVariable(value = "id") long idTest,
                                              @PathVariable(value = "number") int taskNumber,
            @RequestBody AnswerRequestBody answerRequestBody) {

        Task nextTask = answerService.answer(idTest, taskNumber, answerRequestBody);
        return new ResponseEntity<>(nextTask, HttpStatus.OK);
    }
}