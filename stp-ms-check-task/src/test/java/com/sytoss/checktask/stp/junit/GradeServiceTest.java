package com.sytoss.checktask.stp.junit;

import bom.CheckAnswerRequestBody;
import com.sytoss.checktask.stp.AbstractApplicationTest;
import com.sytoss.checktask.stp.service.DatabaseHelperService;
import com.sytoss.checktask.stp.service.GradeService;
import com.sytoss.checktask.stp.service.QueryResultConvertor;
import com.sytoss.domain.bom.personalexam.Grade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class GradeServiceTest extends AbstractApplicationTest {

    private final ObjectProvider<DatabaseHelperService> databaseHelperServiceProvider = mock(ObjectProvider.class);

    @Test
    void checkAndGrade() {
        CheckAnswerRequestBody checkAnswerRequestBody = new CheckAnswerRequestBody();
        checkAnswerRequestBody.setAnswer("select * from answer");
        checkAnswerRequestBody.setEtalon("select * from etalon");
        checkAnswerRequestBody.setScript("{databaseChangeLog: [{changeSet: {author: ivan-larin, changes: [{createTable: {columns: [{column: {name: Answer, type: varchar}}], tableName: Answer}}, {createTable: {columns: [{column: {name: Etalon, type: varchar}}], tableName: Etalon}}], id: create_answer}}, {changeSet: {author: ivan-larin, changes: [{insert: {columns: [{column: {name: Answer, value: it_is_answer}}], tableName: Answer}}, {insert: {columns: [{column: {name: Etalon, value: it_is_etalon}}], tableName: Etalon}}], id: insert-answer}}]}");
        when(databaseHelperServiceProvider.getObject()).thenReturn(new DatabaseHelperService(new QueryResultConvertor()));
        GradeService gradeService = new GradeService(databaseHelperServiceProvider);
        Grade grade = gradeService.checkAndGrade(checkAnswerRequestBody);
        Assertions.assertEquals(10, grade.getValue());
        Assertions.assertEquals("ok", grade.getComment());
    }

    @Test
    void checkAndGradeWithWrongAnswer() {
        CheckAnswerRequestBody checkAnswerRequestBody = new CheckAnswerRequestBody();
        checkAnswerRequestBody.setAnswer("select answer");
        checkAnswerRequestBody.setEtalon("select * from etalon");
        checkAnswerRequestBody.setScript("{databaseChangeLog: [{changeSet: {author: ivan-larin, changes: [{createTable: {columns: [{column: {name: Answer, type: varchar}}], tableName: Answer}}, {createTable: {columns: [{column: {name: Etalon, type: varchar}}], tableName: Etalon}}], id: create_answer}}, {changeSet: {author: ivan-larin, changes: [{insert: {columns: [{column: {name: Answer, value: it_is_answer}}], tableName: Answer}}, {insert: {columns: [{column: {name: Etalon, value: it_is_etalon}}], tableName: Etalon}}], id: insert-answer}}]}");
        when(databaseHelperServiceProvider.getObject()).thenReturn(new DatabaseHelperService(new QueryResultConvertor()));
        GradeService gradeService = new GradeService(databaseHelperServiceProvider);
        Grade grade = gradeService.checkAndGrade(checkAnswerRequestBody);
        Assertions.assertEquals(0, grade.getValue());
    }

    @Test
    void checkAndGradeWithWrongEtalon() {
        CheckAnswerRequestBody checkAnswerRequestBody = new CheckAnswerRequestBody();
        checkAnswerRequestBody.setAnswer("select * from answer");
        checkAnswerRequestBody.setEtalon("select etalon");
        checkAnswerRequestBody.setScript("{databaseChangeLog: [{changeSet: {author: ivan-larin, changes: [{createTable: {columns: [{column: {name: Answer, type: varchar}}], tableName: Answer}}, {createTable: {columns: [{column: {name: Etalon, type: varchar}}], tableName: Etalon}}], id: create_answer}}, {changeSet: {author: ivan-larin, changes: [{insert: {columns: [{column: {name: Answer, value: it_is_answer}}], tableName: Answer}}, {insert: {columns: [{column: {name: Etalon, value: it_is_etalon}}], tableName: Etalon}}], id: insert-answer}}]}");
        RequestEntity httpEntity = new RequestEntity(checkAnswerRequestBody,null,null);
        ResponseEntity<String> responseEntity = doPost("/api/task/check", httpEntity, String.class);
        Assertions.assertEquals(406, responseEntity.getStatusCode().value());
    }

    @Test
    void checkAndGradeWithWrongScript() {
        CheckAnswerRequestBody checkAnswerRequestBody = new CheckAnswerRequestBody();
        checkAnswerRequestBody.setAnswer("select * from answer");
        checkAnswerRequestBody.setEtalon("select etalon");
        checkAnswerRequestBody.setScript("{: [{changeSet: {author {columns: [{column: {name: Answer, type: varchar}}], tableName: Answer}}, {createTable: {columns: [{column: {name: Etalon, type: varchar}}], tableName: Etalon}}], id: create_answer}}, {changeSet: {author: ivan-larin, changes: [{insert: {columns: [{column: {name: Answer, value: it_is_answer}}], tableName: Answer}}, {insert: {columns: [{column: {name: Etalon, value: it_is_etalon}}], tableName: Etalon}}], id: insert-answer}}]}");
        RequestEntity httpEntity = new RequestEntity(checkAnswerRequestBody,null,null);
        ResponseEntity<String> responseEntity = doPost("/api/task/check", httpEntity, String.class);
        Assertions.assertEquals(400, responseEntity.getStatusCode().value());
    }

}