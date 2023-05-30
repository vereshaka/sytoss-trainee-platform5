package com.sytoss.checktask.stp.service;

import com.sytoss.checktask.model.CheckTaskParameters;
import com.sytoss.checktask.stp.AbstractApplicationTest;
import com.sytoss.checktask.stp.bdd.other.TestContext;
import com.sytoss.domain.bom.personalexam.Grade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;


class GradeServiceTest extends AbstractApplicationTest {


    @Test
    void checkAndGrade() {
        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setAnswer("select * from answer");
        checkTaskParameters.setEtalon("select * from etalon");
        checkTaskParameters.setScript("{databaseChangeLog: [{changeSet: {author: ivan-larin, changes: [{createTable: {columns: [{column: {name: Answer, type: varchar}}], tableName: Answer}}, {createTable: {columns: [{column: {name: Etalon, type: varchar}}], tableName: Etalon}}], id: create_answer}}, {changeSet: {author: ivan-larin, changes: [{insert: {columns: [{column: {name: Answer, value: it_is_answer}}], tableName: Answer}}, {insert: {columns: [{column: {name: Etalon, value: it_is_etalon}}], tableName: Etalon}}], id: insert-answer}}]}");
        ObjectProvider<DatabaseHelperService> objectProvider = TestContext.getInstance().getDatabaseHelperServiceProvider();
        when(objectProvider.getObject()).thenReturn(new DatabaseHelperService(new QueryResultConvertor()));
        GradeService gradeService = new GradeService(objectProvider);
        Grade grade = gradeService.checkAndGrade(checkTaskParameters);
        Assertions.assertEquals(10, grade.getValue());
        Assertions.assertEquals("ok", grade.getComment());
    }

   /* @Test
    void checkAndGradeWithWrongAnswer() {
        CheckAnswerRequestBody checkAnswerRequestBody = new CheckAnswerRequestBody();
        checkAnswerRequestBody.setAnswer("select answer");
        checkAnswerRequestBody.setEtalon("select * from etalon");
        checkAnswerRequestBody.setScript("{databaseChangeLog: [{changeSet: {author: ivan-larin, changes: [{createTable: {columns: [{column: {name: Answer, type: varchar}}], tableName: Answer}}, {createTable: {columns: [{column: {name: Etalon, type: varchar}}], tableName: Etalon}}], id: create_answer}}, {changeSet: {author: ivan-larin, changes: [{insert: {columns: [{column: {name: Answer, value: it_is_answer}}], tableName: Answer}}, {insert: {columns: [{column: {name: Etalon, value: it_is_etalon}}], tableName: Etalon}}], id: insert-answer}}]}");
        when(databaseHelperServiceProvider.getObject()).thenReturn(new DatabaseHelperService(new QueryResultConvertor()));
        GradeService gradeService = new GradeService(databaseHelperServiceProvider);
        Grade grade = gradeService.checkAndGrade(checkAnswerRequestBody);
        Assertions.assertEquals(0, grade.getValue());
    }*/

    @Test
    void checkAndGradeWithWrongEtalon() {
        CheckTaskParameters checkAnswerRequestBody = new CheckTaskParameters();
        checkAnswerRequestBody.setAnswer("select * from answer");
        checkAnswerRequestBody.setEtalon("select etalon");
        checkAnswerRequestBody.setScript("{databaseChangeLog: [{changeSet: {author: ivan-larin, changes: [{createTable: {columns: [{column: {name: Answer, type: varchar}}], tableName: Answer}}, {createTable: {columns: [{column: {name: Etalon, type: varchar}}], tableName: Etalon}}], id: create_answer}}, {changeSet: {author: ivan-larin, changes: [{insert: {columns: [{column: {name: Answer, value: it_is_answer}}], tableName: Answer}}, {insert: {columns: [{column: {name: Etalon, value: it_is_etalon}}], tableName: Etalon}}], id: insert-answer}}]}");
        RequestEntity httpEntity = new RequestEntity(checkAnswerRequestBody,null,null);
        ResponseEntity<String> responseEntity = doPost("/api/task/check", httpEntity, String.class);
        Assertions.assertEquals(406, responseEntity.getStatusCode().value());
    }

    @Test
    void checkAndGradeWithWrongScript() {
        CheckTaskParameters checkAnswerRequestBody = new CheckTaskParameters();
        checkAnswerRequestBody.setAnswer("select * from answer");
        checkAnswerRequestBody.setEtalon("select etalon");
        checkAnswerRequestBody.setScript("{: [{changeSet: {author {columns: [{column: {name: Answer, type: varchar}}], tableName: Answer}}, {createTable: {columns: [{column: {name: Etalon, type: varchar}}], tableName: Etalon}}], id: create_answer}}, {changeSet: {author: ivan-larin, changes: [{insert: {columns: [{column: {name: Answer, value: it_is_answer}}], tableName: Answer}}, {insert: {columns: [{column: {name: Etalon, value: it_is_etalon}}], tableName: Etalon}}], id: insert-answer}}]}");
        RequestEntity httpEntity = new RequestEntity(checkAnswerRequestBody,null,null);
        ResponseEntity<String> responseEntity = doPost("/api/task/check", httpEntity, String.class);
        Assertions.assertEquals(400, responseEntity.getStatusCode().value());
    }

}