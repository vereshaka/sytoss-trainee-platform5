package com.sytoss.checktask.stp.service;

import com.sytoss.checktask.stp.service.db.H2Executor;
import com.sytoss.domain.bom.checktask.QueryResult;
import com.sytoss.domain.bom.lessons.ConditionType;
import com.sytoss.domain.bom.lessons.TaskCondition;
import com.sytoss.domain.bom.personalexam.CheckRequestParameters;
import com.sytoss.domain.bom.personalexam.CheckTaskParameters;
import com.sytoss.domain.bom.personalexam.Score;
import com.sytoss.stp.test.StpUnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.ObjectProvider;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.*;

import static com.sytoss.stp.test.FileUtils.readFromFile;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ScoreServiceTest extends StpUnitTest {

    @Mock
    private final ObjectProvider<DatabaseHelperService> objectProvider = mock(ObjectProvider.class);

    @Mock
    private ExecutorService executorService;

    @InjectMocks
    private ScoreService scoreService;

    @Test
    void checkAndGradeCorrectAnswer() {
        when(executorService.submit(any(Callable.class))).thenReturn(getFutureFor(new QueryResult()));

        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setRequest("select * from Authors");
        checkTaskParameters.setEtalon("select * from Authors");
        checkTaskParameters.setScript(readFromFile("task-domain/script1.json"));

        Score score = scoreService.checkAndScore(checkTaskParameters);

        Assertions.assertEquals(1, score.getValue());
        Assertions.assertEquals("", score.getComment());
    }

    @Test
    void checkAndGradeCorrectAnswerWithDifferentColumnsOrder() {
        QueryResult answerResult = new QueryResult();
        answerResult.setHeader(List.of("ID", "NAME"));
        HashMap<String, Object> map11 = new LinkedHashMap<>();
        map11.put("ID", 1L);
        map11.put("NAME", "Ivan Ivanov");
        answerResult.addValues(map11);
        HashMap<String, Object> map12 = new LinkedHashMap<>();
        map12.put("ID", 2L);
        map12.put("NAME", "Ivan Petrov");
        answerResult.addValues(map12);

        QueryResult etalonResult = new QueryResult();
        etalonResult.setHeader(List.of("NAME", "ID"));
        HashMap<String, Object> map21 = new LinkedHashMap<>();
        map21.put("NAME", "Ivan Ivanov");
        map21.put("ID", 1L);
        etalonResult.addValues(map21);
        HashMap<String, Object> map22 = new LinkedHashMap<>();
        map22.put("NAME", "Ivan Petrov");
        map22.put("ID", 2L);
        etalonResult.addValues(map22);

        when(executorService.submit(any(Callable.class)))
                .thenReturn(getFutureFor(answerResult))
                .thenReturn(getFutureFor(etalonResult));

        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setRequest("select id,name from Authors");
        checkTaskParameters.setEtalon("select name,id from Authors");
        checkTaskParameters.setScript(readFromFile("task-domain/script1.json"));

        Score score = scoreService.checkAndScore(checkTaskParameters);

        Assertions.assertEquals(1, score.getValue());
        Assertions.assertEquals("", score.getComment());
    }

    @Test
    void checkAndGradeWrongAnswer() {
        QueryResult answerResult = new QueryResult();
        answerResult.setHeader(List.of("ID", "BOOK_NAME", "AUTHOR_ID"));
        HashMap<String, Object> map11 = new LinkedHashMap<>();
        map11.put("ID", 1L);
        map11.put("BOOK_NAME", "Book 1");
        map11.put("AUTHOR_ID", 1);
        answerResult.addValues(map11);
        HashMap<String, Object> map12 = new LinkedHashMap<>();
        map12.put("ID", 2L);
        map12.put("BOOK_NAME", "Book 2");
        map12.put("AUTHOR_ID", 1);
        answerResult.addValues(map12);
        HashMap<String, Object> map13 = new LinkedHashMap<>();
        map13.put("ID", 3L);
        map13.put("BOOK_NAME", "Book 3");
        map13.put("AUTHOR_ID", 1);
        answerResult.addValues(map13);
        HashMap<String, Object> map14 = new LinkedHashMap<>();
        map14.put("ID", 4L);
        map14.put("BOOK_NAME", "Book 4");
        map14.put("AUTHOR_ID", 2);
        answerResult.addValues(map14);
        HashMap<String, Object> map15 = new LinkedHashMap<>();
        map15.put("ID", 5L);
        map15.put("BOOK_NAME", "Book 5");
        map15.put("AUTHOR_ID", 2);
        answerResult.addValues(map15);

        QueryResult etalonResult = new QueryResult();
        etalonResult.setHeader(List.of("NAME", "ID"));
        HashMap<String, Object> map21 = new LinkedHashMap<>();
        map21.put("NAME", "Ivan Ivanov");
        map21.put("ID", 1L);
        etalonResult.addValues(map21);
        HashMap<String, Object> map22 = new LinkedHashMap<>();
        map22.put("NAME", "Ivan Petrov");
        map22.put("ID", 2L);
        etalonResult.addValues(map22);

        when(executorService.submit(any(Callable.class)))
                .thenReturn(getFutureFor(answerResult))
                .thenReturn(getFutureFor(etalonResult));

        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setRequest("select * from Books");
        checkTaskParameters.setEtalon("select * from Authors");
        checkTaskParameters.setScript(readFromFile("task-domain/script1.json"));

        Score score = scoreService.checkAndScore(checkTaskParameters);
        Assertions.assertEquals(0, score.getValue());
        Assertions.assertEquals("Amount of data is different", score.getComment());
    }

    @Test
    public void checkAndGradeWithoutConditions() {
        QueryResult answerResult = new QueryResult();
        answerResult.setHeader(List.of("ID", "NAME"));
        HashMap<String, Object> map12 = new LinkedHashMap<>();
        map12.put("ID", 2L);
        map12.put("NAME", "Ivan Petrov");
        answerResult.addValues(map12);
        HashMap<String, Object> map11 = new LinkedHashMap<>();
        map11.put("ID", 1L);
        map11.put("NAME", "Ivan Ivanov");
        answerResult.addValues(map11);

        QueryResult etalonResult = new QueryResult();
        etalonResult.setHeader(List.of("NAME", "ID"));
        HashMap<String, Object> map21 = new LinkedHashMap<>();
        map21.put("NAME", "Ivan Ivanov");
        map21.put("ID", 1L);
        etalonResult.addValues(map21);
        HashMap<String, Object> map22 = new LinkedHashMap<>();
        map22.put("NAME", "Ivan Petrov");
        map22.put("ID", 2L);
        etalonResult.addValues(map22);

        when(executorService.submit(any(Callable.class)))
                .thenReturn(getFutureFor(answerResult))
                .thenReturn(getFutureFor(etalonResult));


        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setRequest("select * from Authors");
        checkTaskParameters.setEtalon("select * from Authors ORDER BY Name");
        TaskCondition taskCondition = new TaskCondition();
        taskCondition.setValue("ORDER BY");
        taskCondition.setType(ConditionType.CONTAINS);
        checkTaskParameters.getConditions().add(taskCondition);
        checkTaskParameters.setScript("{databaseChangeLog: [{changeSet: {id: create-tables,author: ivan-larin,changes: [{createTable: {tableName: Authors,columns: [{column: {name: id,type: int, constraints: {primaryKey: true,nullable: false}}},{column: {name: Name,type: varchar(100),constraints: {nullable: false}}}]}},{createTable: {tableName: Books,columns: [{column: {name: id,type: int,autoIncrement: true,constraints: {primaryKey: true,nullable: false}}},{column: {name: Book_name,type: varchar(100),constraints: {nullable: false}}},{column: {name: Author_id,type: int,constraints: {nullable: false}}}],foreignKeyConstraints: [{foreignKeyConstraint: {baseTableName: Books,baseColumnNames: Author_id,referencedTableName: Authors,referencedColumnNames: id,constraintName: FK_book_author,onDelete: CASCADE}}]}}]}},{changeSet: {id: insert-answer,author: ivan-larin,changes: [{insert: {columns: [{column: {name: id,value: 1}},{column: {name: Name,value: Ivan Ivanov}}],tableName: Authors}},{insert: {columns: [{column: {name: id,value: 2}},{column: {name: Name,value: Ivan Petrov}}],tableName: Authors}},{insert: {columns: [{column: {name: Book_name,value: Book1}},{column: {name: Author_id,value: 1}}],tableName: Books}},{insert: {columns: [{column: {name: Book_name,value: Book2}},{column: {name: Author_id,value: 1}}],tableName: Books}},{insert: {columns: [{column: {name: Book_name,value: Book3}},{column: {name: Author_id,value: 1}}],tableName: Books}},{insert: {columns: [{column: {name: Book_name,value: Book4}},{column: {name: Author_id,value: 2}}],tableName: Books}},{insert: {columns: [{column: {name: Book_name,value: Book5}},{column: {name: Author_id,value: 2}}],tableName: Books}}]}}]}");

        Score score = scoreService.checkAndScore(checkTaskParameters);

        Assertions.assertEquals(0.7, score.getValue());
        Assertions.assertEquals("\"ORDER BY\" condition are failed to check", score.getComment());
    }

    @Test
    void checkAndGradeCorrectAnswerWithDifferentColumnsNumber() {
        QueryResult answerResult = new QueryResult();
        answerResult.setHeader(List.of("ID", "BOOK_NAME", "AUTHOR_ID"));
        HashMap<String, Object> map11 = new LinkedHashMap<>();
        map11.put("ID", 1L);
        map11.put("BOOK_NAME", "Book 1");
        map11.put("AUTHOR_ID", 1);
        answerResult.addValues(map11);
        HashMap<String, Object> map12 = new LinkedHashMap<>();
        map12.put("ID", 2L);
        map12.put("BOOK_NAME", "Book 2");
        map12.put("AUTHOR_ID", 1);
        answerResult.addValues(map12);
        HashMap<String, Object> map13 = new LinkedHashMap<>();
        map13.put("ID", 3L);
        map13.put("BOOK_NAME", "Book 3");
        map13.put("AUTHOR_ID", 1);
        answerResult.addValues(map13);
        HashMap<String, Object> map14 = new LinkedHashMap<>();
        map14.put("ID", 4L);
        map14.put("BOOK_NAME", "Book 4");
        map14.put("AUTHOR_ID", 2);
        answerResult.addValues(map14);
        HashMap<String, Object> map15 = new LinkedHashMap<>();
        map15.put("ID", 5L);
        map15.put("BOOK_NAME", "Book 5");
        map15.put("AUTHOR_ID", 2);
        answerResult.addValues(map15);

        QueryResult etalonResult = new QueryResult();
        etalonResult.setHeader(List.of("ID", "BOOK_NAME"));
        HashMap<String, Object> map21 = new LinkedHashMap<>();
        map21.put("ID", 1L);
        map21.put("BOOK_NAME", "Book 1");
        etalonResult.addValues(map21);
        HashMap<String, Object> map22 = new LinkedHashMap<>();
        map22.put("ID", 2L);
        map22.put("BOOK_NAME", "Book 2");
        etalonResult.addValues(map22);
        HashMap<String, Object> map23 = new LinkedHashMap<>();
        map23.put("ID", 3L);
        map23.put("BOOK_NAME", "Book 3");
        etalonResult.addValues(map23);
        HashMap<String, Object> map24 = new LinkedHashMap<>();
        map24.put("ID", 4L);
        map24.put("BOOK_NAME", "Book 4");
        etalonResult.addValues(map24);
        HashMap<String, Object> map25 = new LinkedHashMap<>();
        map25.put("ID", 5L);
        map25.put("BOOK_NAME", "Book 5");
        etalonResult.addValues(map25);

        when(executorService.submit(any(Callable.class)))
                .thenReturn(getFutureFor(answerResult))
                .thenReturn(getFutureFor(etalonResult));

        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setRequest("select id,book_name,author_id from Books");
        checkTaskParameters.setEtalon("select book_name,id from Books");
        checkTaskParameters.setScript(readFromFile("task-domain/script1.json"));

        Score score = scoreService.checkAndScore(checkTaskParameters);

        Assertions.assertEquals(1, score.getValue());
        Assertions.assertEquals("There are more columns in the answer than in the etalon", score.getComment());
    }

    @Test
    void checkAndGradeWrongData() {
        QueryResult answerResult = new QueryResult();
        answerResult.setHeader(List.of("COUNT"));
        HashMap<String, Object> map11 = new LinkedHashMap<>();
        map11.put("COUNT", 2);
        answerResult.addValues(map11);

        QueryResult etalonResult = new QueryResult();
        etalonResult.setHeader(List.of("SUM"));
        HashMap<String, Object> map21 = new LinkedHashMap<>();
        map21.put("SUM", 3);
        etalonResult.addValues(map21);

        when(executorService.submit(any(Callable.class)))
                .thenReturn(getFutureFor(answerResult))
                .thenReturn(getFutureFor(etalonResult));

        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setRequest("select count(*) from Authors");
        checkTaskParameters.setEtalon("select sum(id) from Authors");
        checkTaskParameters.setScript(readFromFile("task-domain/script1.json"));

        Score score = scoreService.checkAndScore(checkTaskParameters);

        Assertions.assertEquals(0, score.getValue());
        Assertions.assertEquals("Request data is wrong comparable to etalon data", score.getComment());
    }

    @Test
    void checkAndGradeWrongData2() {
        QueryResult answerResult = new QueryResult();
        answerResult.setHeader(List.of("ID", "NAME"));
        HashMap<String, Object> map11 = new HashMap<>();
        map11.put("ID", 2L);
        map11.put("NAME", "MySQL");
        answerResult.addValues(map11);
        HashMap<String, Object> map12 = new HashMap<>();
        map12.put("ID", 3L);
        map12.put("NAME", "PostgreSQL");
        answerResult.addValues(map12);

        QueryResult etalonResult = new QueryResult();
        etalonResult.setHeader(List.of("ID", "NAME"));
        HashMap<String, Object> map21 = new HashMap<>();
        map21.put("ID", 1L);
        map21.put("NAME", "MSSQL");
        etalonResult.addValues(map21);
        HashMap<String, Object> map22 = new HashMap<>();
        map22.put("ID", 3L);
        map22.put("NAME", "PostgreSQL");
        etalonResult.addValues(map22);

        when(executorService.submit(any(Callable.class))).thenReturn(getFutureFor(answerResult), getFutureFor(etalonResult));

        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setRequest("select name from Authors");
        checkTaskParameters.setEtalon("select id from Authors");
        checkTaskParameters.setScript(readFromFile("task-domain/script1.json"));

        Score score = scoreService.checkAndScore(checkTaskParameters);

        Assertions.assertEquals(0, score.getValue());
        Assertions.assertEquals("Request data is wrong comparable to etalon data", score.getComment());
    }

    @Test
    void checkAndGradeEtalonColumnsNotFound() {
        QueryResult answerResult = new QueryResult();
        answerResult.setHeader(List.of("NAME"));
        HashMap<String, Object> map11 = new LinkedHashMap<>();
        map11.put("NAME", "Ivan Ivanov");
        answerResult.addValues(map11);
        HashMap<String, Object> map12 = new LinkedHashMap<>();
        map12.put("NAME", "Ivan Petrov");
        answerResult.addValues(map12);

        QueryResult etalonResult = new QueryResult();
        etalonResult.setHeader(List.of("NAME", "ID"));
        HashMap<String, Object> map21 = new LinkedHashMap<>();
        map21.put("NAME", "Ivan Ivanov");
        map21.put("ID", 1L);
        etalonResult.addValues(map21);
        HashMap<String, Object> map22 = new LinkedHashMap<>();
        map22.put("NAME", "Ivan Petrov");
        map22.put("ID", 2L);
        etalonResult.addValues(map22);

        when(executorService.submit(any(Callable.class)))
                .thenReturn(getFutureFor(answerResult))
                .thenReturn(getFutureFor(etalonResult));

        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setRequest("select name from Authors");
        checkTaskParameters.setEtalon("select name,id from Authors");
        checkTaskParameters.setScript(readFromFile("task-domain/script1.json"));

        Score score = scoreService.checkAndScore(checkTaskParameters);

        Assertions.assertEquals(0, score.getValue());
        Assertions.assertEquals("ID columns are absent in the answer", score.getComment());
    }

    @Test
    void checkForInsert() {
        CheckRequestParameters checkRequestParameters = new CheckRequestParameters();
        checkRequestParameters.setRequest("insert into Authors(id,name) values (100,'Author 2')");
        checkRequestParameters.setCheckAnswer("select * from Authors");
        checkRequestParameters.setScript(readFromFile("task-domain/script1.json"));

        when(objectProvider.getObject()).thenReturn(
                new DatabaseHelperService(new QueryResultConvertor(), new H2Executor()),
                new DatabaseHelperService(new QueryResultConvertor(), new H2Executor())
        );

        scoreService.checkRequest(checkRequestParameters);
    }

    private Future getFutureFor(QueryResult queryResult) {
        return new Future() {

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public Object get() {
                return queryResult;
            }

            @Override
            public Object get(long timeout, TimeUnit unit) {
                return null;
            }
        };
    }

}
