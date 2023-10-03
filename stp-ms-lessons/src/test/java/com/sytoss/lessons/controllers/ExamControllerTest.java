package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.examassignee.ExamGroupAssignee;
import com.sytoss.domain.bom.lessons.examassignee.ExamStudentAssignee;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import com.sytoss.lessons.dto.TopicDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
        LinkedHashMap<String, Object> user = new LinkedHashMap<>();
        user.put("id", 1);
        user.put("firstName", "John");
        user.put("lastName", "Doe");
        user.put("email", "john.doe@email.com");
        when(userConnector.getMyProfile()).thenReturn(user);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Exam> response = doDelete("/api/exam/1/delete", httpEntity, Exam.class);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void shouldAssignExamToGroups() {
        LinkedHashMap<String, Object> user = new LinkedHashMap<>();
        user.put("id", 1);
        user.put("firstName", "John");
        user.put("lastName", "Doe");
        user.put("email", "john.doe@email.com");

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

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

        Student student = new Student();
        student.setId(1L);
        student.setPrimaryGroup(group);

        ExamGroupAssignee examGroupAssignee = new ExamGroupAssignee();
        examGroupAssignee.setRelevantFrom(new Date());
        examGroupAssignee.setRelevantTo(new Date());
        examGroupAssignee.getGroups().add(group);

        when(userConnector.getMyProfile()).thenReturn(user);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(examGroupAssignee, httpHeaders);
        ResponseEntity<Exam> response = doPost("/api/exam/assign/" + exam.getId() + "/students", httpEntity, Exam.class);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void shouldAssignExamToStudents() {
        LinkedHashMap<String, Object> user = new LinkedHashMap<>();
        user.put("id", 1);
        user.put("firstName", "John");
        user.put("lastName", "Doe");
        user.put("email", "john.doe@email.com");

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

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

        ExamStudentAssignee examStudentAssignee = new ExamStudentAssignee();
        examStudentAssignee.setRelevantFrom(new Date());
        examStudentAssignee.setRelevantTo(new Date());
        examStudentAssignee.getStudents().add(student);

        when(userConnector.getMyProfile()).thenReturn(user);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(examStudentAssignee, httpHeaders);
        ResponseEntity<Exam> response = doPost("/api/exam/assign/" + exam.getId() + "/students", httpEntity, Exam.class);
        assertEquals(200, response.getStatusCode().value());
    }
}
