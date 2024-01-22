package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.exceptions.business.PersonalExamIntegrationException;
import com.sytoss.domain.bom.exceptions.business.notfound.ExamNotFoundException;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import com.sytoss.lessons.dto.TopicDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class ExamControllerTest extends LessonsControllerTest {

    @Test
    public void shouldSaveExam() {
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<Exam> httpEntity = new HttpEntity<>(new Exam(), httpHeaders);
        ResponseEntity<Exam> response = doPost("/api/exam/save", httpEntity, Exam.class);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void shouldDeleteExam() {
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Exam> response = doDelete("/api/exam/1/delete", httpEntity, Exam.class);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void shouldAssignExamToGroups() {
        ExamDTO exam = new ExamDTO();
        exam.setId(1L);
        exam.setName("Exam");
        exam.setNumberOfTasks(10);
        exam.setTopics(List.of(new TopicDTO(), new TopicDTO()));
        exam.setTeacherId(1L);
        TaskDTO task = new TaskDTO();
        task.setTaskDomain(new TaskDomainDTO());
        exam.setTasks(List.of(task));

        when(examConnector.getReferenceById(any())).thenReturn(exam);

        Group group = new Group();
        group.setId(1L);
        group.setName("AT-18");

        ExamAssignee examGroupAssignee = new ExamAssignee();
        examGroupAssignee.setRelevantFrom(new Date());
        examGroupAssignee.setRelevantTo(new Date());
        examGroupAssignee.getGroups().add(group);

        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(examGroupAssignee, httpHeaders);
        ResponseEntity<Exam> response = doPost("/api/exam/" + exam.getId() + "/assign", httpEntity, Exam.class);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void shouldAssignExamToStudents() {
        ExamDTO exam = new ExamDTO();
        exam.setId(1L);
        exam.setName("Exam");
        exam.setNumberOfTasks(10);
        exam.setTopics(List.of(new TopicDTO(), new TopicDTO()));
        exam.setTeacherId(1L);
        TaskDTO task = new TaskDTO();
        task.setTaskDomain(new TaskDomainDTO());
        exam.setTasks(List.of(task));
        when(examConnector.getReferenceById(any())).thenReturn(exam);

        Student student = new Student();
        student.setId(1L);

        ExamAssignee examStudentAssignee = new ExamAssignee();
        examStudentAssignee.setRelevantFrom(new Date());
        examStudentAssignee.setRelevantTo(new Date());
        examStudentAssignee.getStudents().add(student);

        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(examStudentAssignee, httpHeaders);
        ResponseEntity<Exam> response = doPost("/api/exam/" + exam.getId() + "/assign", httpEntity, Exam.class);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void shouldUpdateExam() {
        Exam exam = new Exam();
        exam.setId(1L);

        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(exam, httpHeaders);
        ResponseEntity<Void> response = doPut("/api/exam/" + exam.getId(), httpEntity, Void.class);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void examUpdateShouldFailWith404() {
        Exam exam = new Exam();
        exam.setId(1L);

        doThrow(ExamNotFoundException.class).when(examService).update(eq(1L), any(Exam.class));

        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(exam, httpHeaders);
        ResponseEntity<Exam> response = doPut("/api/exam/" + exam.getId(), httpEntity, Exam.class);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void examUpdateShouldFailWith400() {
        Exam exam = new Exam();
        exam.setId(1L);

        doThrow(PersonalExamIntegrationException.class).when(examService).update(eq(1L), any(Exam.class));

        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(exam, httpHeaders);
        ResponseEntity<Exam> response = doPut("/api/exam/" + exam.getId(), httpEntity, Exam.class);
        assertEquals(400, response.getStatusCode().value());
    }
}
