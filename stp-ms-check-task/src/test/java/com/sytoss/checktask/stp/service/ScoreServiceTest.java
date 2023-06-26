package com.sytoss.checktask.stp.service;

import com.sytoss.checktask.model.CheckTaskParameters;
import com.sytoss.checktask.stp.AbstractApplicationTest;
import com.sytoss.checktask.stp.bdd.other.TestContext;
import com.sytoss.domain.bom.lessons.ConditionType;
import com.sytoss.domain.bom.lessons.TaskCondition;
import com.sytoss.domain.bom.personalexam.Score;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.mockito.Mockito.when;


class ScoreServiceTest extends AbstractApplicationTest {

    @Test
    void checkAndGradeCorrectAnswer() throws IOException {
        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setAnswer("select * from Authors");
        checkTaskParameters.setEtalon("select * from Authors");
        checkTaskParameters.setScript(readFromFile("script1.json"));
        ObjectProvider<DatabaseHelperService> objectProvider = TestContext.getInstance().getDatabaseHelperServiceProvider();
        when(objectProvider.getObject()).thenReturn(new DatabaseHelperService(new QueryResultConvertor()));
        ScoreService scoreService = new ScoreService(objectProvider);
        Score score = scoreService.checkAndScore(checkTaskParameters);
        Assertions.assertEquals(1, score.getValue());
        Assertions.assertEquals("ok", score.getComment());
    }

    @Test
    void checkAndGradeCorrectAnswerWithDifferentColumnsOrder() throws IOException {
        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setAnswer("select id,name from Authors");
        checkTaskParameters.setEtalon("select name,id from Authors");
        checkTaskParameters.setScript(readFromFile("script1.json"));
        ObjectProvider<DatabaseHelperService> objectProvider = TestContext.getInstance().getDatabaseHelperServiceProvider();
        when(objectProvider.getObject()).thenReturn(new DatabaseHelperService(new QueryResultConvertor()));
        ScoreService scoreService = new ScoreService(objectProvider);
        Score score = scoreService.checkAndScore(checkTaskParameters);
        Assertions.assertEquals(1, score.getValue());
        Assertions.assertEquals("ok", score.getComment());
    }

    @Test
    void checkAndGradeWrongAnswer() throws IOException {
        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setAnswer("select * from Books");
        checkTaskParameters.setEtalon("select * from Authors");
        checkTaskParameters.setScript(readFromFile("script1.json"));
        ObjectProvider<DatabaseHelperService> objectProvider = TestContext.getInstance().getDatabaseHelperServiceProvider();
        when(objectProvider.getObject()).thenReturn(new DatabaseHelperService(new QueryResultConvertor()));
        ScoreService scoreService = new ScoreService(objectProvider);
        Score score = scoreService.checkAndScore(checkTaskParameters);
        Assertions.assertEquals(0, score.getValue());
        Assertions.assertEquals("not ok", score.getComment());
    }

    @Test
    void checkAndGradeWithWrongEtalon() throws IOException {
        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setAnswer("select * from Authors");
        checkTaskParameters.setEtalon("select  Authors");
        checkTaskParameters.setScript(readFromFile("script1.json"));
        RequestEntity httpEntity = new RequestEntity(checkTaskParameters, null, null);
        ResponseEntity<String> responseEntity = doPost("/api/task/check", httpEntity, String.class);
        Assertions.assertEquals(406, responseEntity.getStatusCode().value());
    }

    @Test
    void checkAndGradeWithWrongScript() {
        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setAnswer("select * from Authors");
        checkTaskParameters.setEtalon("select * from Authors");
        checkTaskParameters.setScript("Wrong script");
        RequestEntity httpEntity = new RequestEntity(checkTaskParameters, null, null);
        ResponseEntity<String> responseEntity = doPost("/api/task/check", httpEntity, String.class);
        Assertions.assertEquals(400, responseEntity.getStatusCode().value());
    }

    private String readFromFile(String script) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("scripts/" + script).getFile());
        List<String> data = Files.readAllLines(Path.of(file.getPath()));
        return String.join("\n", data);
    }

    @Test
    public void checkAndGradeWithoutConditions(){
        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setAnswer("select * from Authors");
        checkTaskParameters.setEtalon("select * from Authors ORDER BY Name");
        TaskCondition taskCondition = new TaskCondition();
        taskCondition.setValue("ORDER BY");
        taskCondition.setType(ConditionType.CONTAINS);
        checkTaskParameters.getConditions().add(taskCondition);
        checkTaskParameters.setScript("{databaseChangeLog: [{changeSet: {id: create-tables,author: ivan-larin,changes: [{createTable: {tableName: Authors,columns: [{column: {name: id,type: int, constraints: {primaryKey: true,nullable: false}}},{column: {name: Name,type: varchar,constraints: {nullable: false}}}]}},{createTable: {tableName: Books,columns: [{column: {name: id,type: int,autoIncrement: true,constraints: {primaryKey: true,nullable: false}}},{column: {name: Book_name,type: varchar,constraints: {nullable: false}}},{column: {name: Author_id,type: int,constraints: {nullable: false}}}],foreignKeyConstraints: [{foreignKeyConstraint: {baseTableName: Books,baseColumnNames: Author_id,referencedTableName: Authors,referencedColumnNames: id,constraintName: FK_book_author,onDelete: CASCADE}}]}}]}},{changeSet: {id: insert-answer,author: ivan-larin,changes: [{insert: {columns: [{column: {name: id,value: 1}},{column: {name: Name,value: Ivan Ivanov}}],tableName: Authors}},{insert: {columns: [{column: {name: id,value: 2}},{column: {name: Name,value: Ivan Petrov}}],tableName: Authors}},{insert: {columns: [{column: {name: Book_name,value: Book1}},{column: {name: Author_id,value: 1}}],tableName: Books}},{insert: {columns: [{column: {name: Book_name,value: Book2}},{column: {name: Author_id,value: 1}}],tableName: Books}},{insert: {columns: [{column: {name: Book_name,value: Book3}},{column: {name: Author_id,value: 1}}],tableName: Books}},{insert: {columns: [{column: {name: Book_name,value: Book4}},{column: {name: Author_id,value: 2}}],tableName: Books}},{insert: {columns: [{column: {name: Book_name,value: Book5}},{column: {name: Author_id,value: 2}}],tableName: Books}}]}}]}");
        ObjectProvider<DatabaseHelperService> objectProvider = TestContext.getInstance().getDatabaseHelperServiceProvider();
        when(objectProvider.getObject()).thenReturn(new DatabaseHelperService(new QueryResultConvertor()));
        ScoreService scoreService = new ScoreService(objectProvider);
        Score score = scoreService.checkAndScore(checkTaskParameters);
        Assertions.assertEquals(0.7, score.getValue());
        Assertions.assertEquals("ok", score.getComment());
    }
}