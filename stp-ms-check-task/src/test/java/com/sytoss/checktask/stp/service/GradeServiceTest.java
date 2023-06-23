package com.sytoss.checktask.stp.service;

import com.sytoss.checktask.model.CheckTaskParameters;
import com.sytoss.domain.bom.lessons.ConditionType;
import com.sytoss.domain.bom.lessons.TaskCondition;
import com.sytoss.domain.bom.personalexam.Grade;
import com.sytoss.stp.test.StpUnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.ObjectProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.mockito.Mockito.when;

class GradeServiceTest extends StpUnitTest {

    @Mock
    private ObjectProvider<DatabaseHelperService> objectProvider;

    @InjectMocks
    private GradeService gradeService;

    @Test
    void checkAndGradeCorrectAnswer() {
        when(objectProvider.getObject()).thenReturn(new DatabaseHelperService(new QueryResultConvertor()));

        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setAnswer("select * from Authors");
        checkTaskParameters.setEtalon("select * from Authors");
        checkTaskParameters.setScript(readFromFile("script1.json"));

        Grade grade = gradeService.checkAndGrade(checkTaskParameters);

        Assertions.assertEquals(1, grade.getValue());
        Assertions.assertEquals("ok", grade.getComment());
    }

    @Test
    void checkAndGradeCorrectAnswerWithDifferentColumnsOrder() {
        when(objectProvider.getObject()).thenReturn(new DatabaseHelperService(new QueryResultConvertor()));

        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setAnswer("select id,name from Authors");
        checkTaskParameters.setEtalon("select name,id from Authors");
        checkTaskParameters.setScript(readFromFile("script1.json"));

        Grade grade = gradeService.checkAndGrade(checkTaskParameters);

        Assertions.assertEquals(1, grade.getValue());
        Assertions.assertEquals("ok", grade.getComment());
    }

    @Test
    void checkAndGradeWrongAnswer() {
        when(objectProvider.getObject()).thenReturn(new DatabaseHelperService(new QueryResultConvertor()));

        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setAnswer("select * from Books");
        checkTaskParameters.setEtalon("select * from Authors");
        checkTaskParameters.setScript(readFromFile("script1.json"));

        Grade grade = gradeService.checkAndGrade(checkTaskParameters);

        Assertions.assertEquals(0, grade.getValue());
        Assertions.assertEquals("not ok", grade.getComment());
    }

    @Test
    public void checkAndGradeWithoutConditions() {
        when(objectProvider.getObject()).thenReturn(new DatabaseHelperService(new QueryResultConvertor()));

        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setAnswer("select * from Authors");
        checkTaskParameters.setEtalon("select * from Authors ORDER BY Name");
        TaskCondition taskCondition = new TaskCondition();
        taskCondition.setValue("ORDER BY");
        taskCondition.setType(ConditionType.CONTAINS);
        checkTaskParameters.getConditions().add(taskCondition);
        checkTaskParameters.setScript("{databaseChangeLog: [{changeSet: {id: create-tables,author: ivan-larin,changes: [{createTable: {tableName: Authors,columns: [{column: {name: id,type: int, constraints: {primaryKey: true,nullable: false}}},{column: {name: Name,type: varchar,constraints: {nullable: false}}}]}},{createTable: {tableName: Books,columns: [{column: {name: id,type: int,autoIncrement: true,constraints: {primaryKey: true,nullable: false}}},{column: {name: Book_name,type: varchar,constraints: {nullable: false}}},{column: {name: Author_id,type: int,constraints: {nullable: false}}}],foreignKeyConstraints: [{foreignKeyConstraint: {baseTableName: Books,baseColumnNames: Author_id,referencedTableName: Authors,referencedColumnNames: id,constraintName: FK_book_author,onDelete: CASCADE}}]}}]}},{changeSet: {id: insert-answer,author: ivan-larin,changes: [{insert: {columns: [{column: {name: id,value: 1}},{column: {name: Name,value: Ivan Ivanov}}],tableName: Authors}},{insert: {columns: [{column: {name: id,value: 2}},{column: {name: Name,value: Ivan Petrov}}],tableName: Authors}},{insert: {columns: [{column: {name: Book_name,value: Book1}},{column: {name: Author_id,value: 1}}],tableName: Books}},{insert: {columns: [{column: {name: Book_name,value: Book2}},{column: {name: Author_id,value: 1}}],tableName: Books}},{insert: {columns: [{column: {name: Book_name,value: Book3}},{column: {name: Author_id,value: 1}}],tableName: Books}},{insert: {columns: [{column: {name: Book_name,value: Book4}},{column: {name: Author_id,value: 2}}],tableName: Books}},{insert: {columns: [{column: {name: Book_name,value: Book5}},{column: {name: Author_id,value: 2}}],tableName: Books}}]}}]}");

        Grade grade = gradeService.checkAndGrade(checkTaskParameters);

        Assertions.assertEquals(0.7, grade.getValue());
        Assertions.assertEquals("ok", grade.getComment());
    }

    private String readFromFile(String script) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("scripts/" + script).getFile());
        List<String> data = null;
        try {
            data = Files.readAllLines(Path.of(file.getPath()));
        } catch (IOException e) {
            throw new RuntimeException("Could not load script data. Script: " + script, e);
        }
        return String.join("\n", data);
    }
}
