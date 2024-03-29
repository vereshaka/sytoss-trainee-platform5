package com.sytoss.producer.controllers;

import com.sytoss.domain.bom.exceptions.business.PersonalExamAlreadyStartedException;
import com.sytoss.domain.bom.exceptions.business.notfound.PersonalExamNotFoundException;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.personalexam.*;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class PersonalExamControllerTest extends TestProducerControllerTest {

    @Test
    public void shouldCreateExam() {
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        PersonalExam personalExam = new PersonalExam();
        Answer answer = new Answer();
        answer.setStatus(AnswerStatus.ANSWERED);
        personalExam.setAnswers(List.of(answer));
        when(personalExamService.create(any())).thenReturn(personalExam);
        HttpEntity<ExamConfiguration> requestEntity = new HttpEntity<>(new ExamConfiguration(), httpHeaders);
        ResponseEntity<PersonalExam> result = doPost("/api/personal-exam/create", requestEntity, PersonalExam.class);
        assertEquals(200, result.getStatusCode().value());
        personalExam = result.getBody();
        assertEquals(0, personalExam.getAnswers().size());
    }

    @Test
    public void shouldReturnBooleanConditionIfTaskDomainIsUsed() {
        when(personalExamService.taskDomainIsUsed(anyLong())).thenReturn(true);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Boolean> result = doGet("/api/personal-exam/is-used-now/task-domain/123", requestEntity, boolean.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldSummaryExam() {
        PersonalExam personalExam = new PersonalExam();
        Answer answer = new Answer();
        answer.setStatus(AnswerStatus.ANSWERED);
        personalExam.setAnswers(List.of(answer));
        when(personalExamService.summary(any())).thenReturn(personalExam);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<PersonalExam> result = doGet("/api/personal-exam/123/summary", requestEntity, PersonalExam.class);
        assertEquals(200, result.getStatusCode().value());
        personalExam = result.getBody();
        assertEquals(1, personalExam.getAnswers().size());
    }

    @Test
    public void shouldReturnQuestionImage() {
        when(personalExamService.getQuestionImage("123-abc-def")).thenReturn(new byte[]{});
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<byte[]> result = doGet("/api/personal-exam/123-abc-def/task/question", httpEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldStartTest() {
        when(personalExamService.start(anyString())).thenReturn(new Question());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<Question> result = doGet("/api/personal-exam/123/start", requestEntity, Question.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void testAnswer() {
        String examId = "123";
        String taskAnswer = "taskAnswer";
        Date uiDate = new Date();
        Long timeSpent = 10L;
        Question expectedAnswer = new Question();
        AnswerModule answerModule = new AnswerModule();
        answerModule.setAnswer(taskAnswer);
        answerModule.setAnswerUIDate(uiDate);
        answerModule.setTimeSpent(timeSpent);

        when(answerService.answer(examId, taskAnswer, uiDate, timeSpent)).thenReturn(expectedAnswer);

        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AnswerModule> requestEntity = new HttpEntity<>(answerModule, httpHeaders);

        ResponseEntity<Question> result = doPost("/api/personal-exam/12dsa/task/answer", requestEntity, Question.class);
        assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
    }

    @Test
    public void shouldGetPersonalExamByUserId() throws ParseException {
        List<PersonalExam> exams = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        exams.add(createPersonalExam(1L, "Math", 5, format.parse("14.12.2018"), format.parse("14.12.2018")));
        exams.add(createPersonalExam(2L, "SQL", 10, format.parse("14.12.2018"), format.parse("14.12.2018")));

        HttpHeaders httpHeaders = getDefaultHttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        when(personalExamService.getByStudentId(1L)).thenReturn(exams);

        ResponseEntity<List<Task>> result = doGet("/api/personal-exam/teacher/1", httpEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void shouldDeletePersonalExamsByExamId() {
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<PersonalExam>> result = doDelete("/api/personal-exam/exam/assignee/1", httpEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    private PersonalExam createPersonalExam(Long examAssigneeId, String name, int amountOfTasks, Date assignedDate, Date startedDate) {
        PersonalExam personalExam = new PersonalExam();
        personalExam.setExamAssigneeId(examAssigneeId);
        personalExam.setName(name);
        personalExam.finish();
        personalExam.setAmountOfTasks(amountOfTasks);
        personalExam.setAssignedDate(assignedDate);
        personalExam.setStartedDate(startedDate);

        return personalExam;
    }

    @Test
    public void shouldReviewByAnswers() {
        Answer answer1 = new Answer();
        answer1.setValue("select * from products");
        answer1.setGrade(new Grade(1, "answer correct"));
        answer1.setStatus(AnswerStatus.GRADED);
        Answer answer2 = new Answer();
        answer2.setValue("select * from products");
        answer2.setGrade(new Grade(1, "answer correct"));
        answer2.setStatus(AnswerStatus.GRADED);
        answer1.setId(1L);
        answer2.setId(2L);

        PersonalExam personalExam = new PersonalExam();
        personalExam.setId("1");
        personalExam.setName("DDL requests");
        personalExam.setAnswers(List.of(answer1));

        PersonalExam personalExam2 = new PersonalExam();
        personalExam2.setId("2");
        personalExam2.setName("DDL requests");
        personalExam2.setAnswers(List.of(answer2));

        when(personalExamService.getById("1")).thenReturn(personalExam);
        when(personalExamService.getById("2")).thenReturn(personalExam2);

        answer1.setTeacherGrade(new Grade(20, "ok"));
        answer2.setTeacherGrade(new Grade(10, "ok"));

        ExamAssigneeAnswersModel examAssigneeAnswersModel = new ExamAssigneeAnswersModel();
        ReviewGradeModel gradeModel1 = new ReviewGradeModel();
        gradeModel1.setPersonalExamId(personalExam.getId());
        gradeModel1.setAnswer(answer1);
        ReviewGradeModel gradeModel2 = new ReviewGradeModel();
        gradeModel2.setPersonalExamId(personalExam2.getId());
        gradeModel2.setAnswer(answer2);
        examAssigneeAnswersModel.setGrades(List.of(gradeModel1, gradeModel2));

        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ExamAssigneeAnswersModel> httpEntity = new HttpEntity<>(examAssigneeAnswersModel, httpHeaders);

        ResponseEntity<ExamAssigneeAnswersModel> result = doPost("/api/personal-exam/review/answers", httpEntity, ExamAssigneeAnswersModel.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void updateTask() {
        Task task = new Task();
        task.setId(1L);
        task.setQuestion("select all from products");

        Answer answer1 = new Answer();
        answer1.setValue("select * from products");
        answer1.setGrade(new Grade(1, "answer correct"));
        answer1.setStatus(AnswerStatus.GRADED);
        answer1.setTask(task);
        Answer answer2 = new Answer();
        answer2.setValue("select * from products");
        answer2.setGrade(new Grade(1, "answer correct"));
        answer2.setStatus(AnswerStatus.GRADED);
        answer2.setTask(task);
        answer1.setId(1L);
        answer2.setId(2L);

        PersonalExam personalExam = new PersonalExam();
        personalExam.setId("1");
        personalExam.setName("DDL requests");
        personalExam.setAnswers(List.of(answer1));
        personalExam.review();

        PersonalExam personalExam2 = new PersonalExam();
        personalExam2.setId("2");
        personalExam2.setName("DDL requests");
        personalExam2.setAnswers(List.of(answer2));

        when(personalExamService.getById("1")).thenReturn(personalExam);
        when(personalExamService.getById("2")).thenReturn(personalExam2);

        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Task> httpEntity = new HttpEntity<>(task, httpHeaders);

        ResponseEntity<List<PersonalExam>> result = doPost("/api/personal-exam/task/update", httpEntity, new ParameterizedTypeReference<>() {});
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void personalExamUpdateShouldFailWith404() {
        ExamConfiguration examConfiguration = new ExamConfiguration();

        doThrow(PersonalExamNotFoundException.class).when(personalExamService).update(any());

        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<ExamConfiguration> requestBody = new HttpEntity<>(examConfiguration, httpHeaders);

        ResponseEntity<Void> response = doPut("/api/personal-exam/update", requestBody, Void.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void personalExamUpdateShouldFailWith409() {
        ExamConfiguration examConfiguration = new ExamConfiguration();

        doThrow(PersonalExamAlreadyStartedException.class).when(personalExamService).update(any());

        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<ExamConfiguration> requestBody = new HttpEntity<>(examConfiguration, httpHeaders);

        ResponseEntity<Void> response = doPut("/api/personal-exam/update", requestBody, Void.class);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
}
