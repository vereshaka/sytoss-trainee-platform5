package com.sytoss.checktask.stp.service;

import com.sytoss.domain.bom.personalexam.CheckTaskParameters;
import com.sytoss.domain.bom.lessons.ConditionType;
import com.sytoss.domain.bom.lessons.TaskCondition;
import com.sytoss.domain.bom.personalexam.Score;
import com.sytoss.stp.test.StpUnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.ObjectProvider;

import java.io.IOException;

import static com.sytoss.stp.test.FileUtils.readFromFile;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class ScoreServiceTest extends StpUnitTest {

    @Mock
    private final ObjectProvider<DatabaseHelperService> objectProvider = mock(ObjectProvider.class);

    @InjectMocks
    private ScoreService scoreService;

    @Test
    void checkAndGradeCorrectAnswer() {
        when(objectProvider.getObject()).thenReturn(new DatabaseHelperService(new QueryResultConvertor()));

        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setRequest("select * from Authors");
        checkTaskParameters.setEtalon("select * from Authors");
        checkTaskParameters.setScript(readFromFile("script1.json"));

        Score score = scoreService.checkAndScore(checkTaskParameters);

        Assertions.assertEquals(1, score.getValue());
        Assertions.assertEquals("ok", score.getComment());
    }

    @Test
    void checkAndGradeCorrectAnswerWithDifferentColumnsOrder() {
        when(objectProvider.getObject()).thenReturn(new DatabaseHelperService(new QueryResultConvertor()));

        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setRequest("select id,name from Authors");
        checkTaskParameters.setEtalon("select name,id from Authors");
        checkTaskParameters.setScript(readFromFile("script1.json"));

        Score score = scoreService.checkAndScore(checkTaskParameters);

        Assertions.assertEquals(1, score.getValue());
        Assertions.assertEquals("ok", score.getComment());
    }

    @Test
    void checkAndGradeWrongAnswer() throws IOException {
        when(objectProvider.getObject()).thenReturn(new DatabaseHelperService(new QueryResultConvertor()));

        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setRequest("select * from Books");
        checkTaskParameters.setEtalon("select * from Authors");
        checkTaskParameters.setScript(readFromFile("script1.json"));

        ScoreService scoreService = new ScoreService(objectProvider);

        Score score = scoreService.checkAndScore(checkTaskParameters);
        Assertions.assertEquals(0, score.getValue());
        Assertions.assertEquals("not ok", score.getComment());
    }

    @Test
    public void checkAndGradeWithoutConditions() {
        when(objectProvider.getObject()).thenReturn(new DatabaseHelperService(new QueryResultConvertor()));

        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setRequest("select * from Authors");
        checkTaskParameters.setEtalon("select * from Authors ORDER BY Name");
        TaskCondition taskCondition = new TaskCondition();
        taskCondition.setValue("ORDER BY");
        taskCondition.setType(ConditionType.CONTAINS);
        checkTaskParameters.getConditions().add(taskCondition);
        checkTaskParameters.setScript("{databaseChangeLog: [{changeSet: {id: create-tables,author: ivan-larin,changes: [{createTable: {tableName: Authors,columns: [{column: {name: id,type: int, constraints: {primaryKey: true,nullable: false}}},{column: {name: Name,type: varchar,constraints: {nullable: false}}}]}},{createTable: {tableName: Books,columns: [{column: {name: id,type: int,autoIncrement: true,constraints: {primaryKey: true,nullable: false}}},{column: {name: Book_name,type: varchar,constraints: {nullable: false}}},{column: {name: Author_id,type: int,constraints: {nullable: false}}}],foreignKeyConstraints: [{foreignKeyConstraint: {baseTableName: Books,baseColumnNames: Author_id,referencedTableName: Authors,referencedColumnNames: id,constraintName: FK_book_author,onDelete: CASCADE}}]}}]}},{changeSet: {id: insert-answer,author: ivan-larin,changes: [{insert: {columns: [{column: {name: id,value: 1}},{column: {name: Name,value: Ivan Ivanov}}],tableName: Authors}},{insert: {columns: [{column: {name: id,value: 2}},{column: {name: Name,value: Ivan Petrov}}],tableName: Authors}},{insert: {columns: [{column: {name: Book_name,value: Book1}},{column: {name: Author_id,value: 1}}],tableName: Books}},{insert: {columns: [{column: {name: Book_name,value: Book2}},{column: {name: Author_id,value: 1}}],tableName: Books}},{insert: {columns: [{column: {name: Book_name,value: Book3}},{column: {name: Author_id,value: 1}}],tableName: Books}},{insert: {columns: [{column: {name: Book_name,value: Book4}},{column: {name: Author_id,value: 2}}],tableName: Books}},{insert: {columns: [{column: {name: Book_name,value: Book5}},{column: {name: Author_id,value: 2}}],tableName: Books}}]}}]}");

        Score score = scoreService.checkAndScore(checkTaskParameters);

        Assertions.assertEquals(0.7, score.getValue());
        Assertions.assertEquals("ok", score.getComment());
    }
}
