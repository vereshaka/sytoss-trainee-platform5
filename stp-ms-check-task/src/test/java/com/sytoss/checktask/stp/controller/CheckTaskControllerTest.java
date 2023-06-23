package com.sytoss.checktask.stp.controller;

import com.sytoss.checktask.model.CheckTaskParameters;
import com.sytoss.checktask.stp.exceptions.DatabaseCommunicationException;
import com.sytoss.checktask.stp.exceptions.WrongEtalonException;
import com.sytoss.checktask.stp.service.DatabaseHelperService;
import com.sytoss.checktask.stp.service.GradeService;
import com.sytoss.domain.bom.personalexam.Grade;
import com.sytoss.stp.test.StpApplicationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CheckTaskControllerTest extends StpApplicationTest {

    @MockBean
    private GradeService gradeService;

    @InjectMocks
    private CheckTaskController checkTaskController;

    @Test
    void checkAndGradeWithWrongEtalon() {
        when(gradeService.checkAndGrade(any())).thenThrow(new WrongEtalonException("", new RuntimeException()));

        CheckTaskParameters checkAnswerRequestBody = new CheckTaskParameters();
        checkAnswerRequestBody.setAnswer("select * from answer");
        checkAnswerRequestBody.setEtalon("select etalon");
        checkAnswerRequestBody.setScript("{databaseChangeLog: [{changeSet: {author: ivan-larin, changes: [{createTable: {columns: [{column: {name: Answer, type: varchar}}], tableName: Answer}}, {createTable: {columns: [{column: {name: Etalon, type: varchar}}], tableName: Etalon}}], id: create_answer}}, {changeSet: {author: ivan-larin, changes: [{insert: {columns: [{column: {name: Answer, value: it_is_answer}}], tableName: Answer}}, {insert: {columns: [{column: {name: Etalon, value: it_is_etalon}}], tableName: Etalon}}], id: insert-answer}}]}");
        RequestEntity httpEntity = new RequestEntity(checkAnswerRequestBody, null, null);

        ResponseEntity<Grade> responseEntity = doPost("/api/task/check", httpEntity, Grade.class);

        Assertions.assertEquals(406, responseEntity.getStatusCode().value());
    }

    @Test
    void checkAndGradeWithWrongScript() {
        when(gradeService.checkAndGrade(any())).thenThrow(new DatabaseCommunicationException("", new RuntimeException()));

        CheckTaskParameters checkAnswerRequestBody = new CheckTaskParameters();
        checkAnswerRequestBody.setAnswer("select * from answer");
        checkAnswerRequestBody.setEtalon("select etalon");
        checkAnswerRequestBody.setScript("{: [{changeSet: {author {columns: [{column: {name: Answer, type: varchar}}], tableName: Answer}}, {createTable: {columns: [{column: {name: Etalon, type: varchar}}], tableName: Etalon}}], id: create_answer}}, {changeSet: {author: ivan-larin, changes: [{insert: {columns: [{column: {name: Answer, value: it_is_answer}}], tableName: Answer}}, {insert: {columns: [{column: {name: Etalon, value: it_is_etalon}}], tableName: Etalon}}], id: insert-answer}}]}");
        RequestEntity httpEntity = new RequestEntity(checkAnswerRequestBody, null, null);

        ResponseEntity<Grade> responseEntity = doPost("/api/task/check", httpEntity, Grade.class);

        Assertions.assertEquals(400, responseEntity.getStatusCode().value());
    }
}
