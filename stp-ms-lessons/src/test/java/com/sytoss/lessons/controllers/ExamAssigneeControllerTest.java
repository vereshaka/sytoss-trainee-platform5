package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.ExamReportModel;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.users.Student;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class ExamAssigneeControllerTest extends LessonsControllerTest {

    @Test
    void getListOfExamAssignee() {
        when(examService.returnExamAssignees(1L)).thenReturn(List.of(new ExamAssignee()));
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<ExamAssignee>> response = doGet("/api/assignee/1/all", httpEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void getExamAssigneeById() {
        when(examService.returnExamAssigneeById(1L)).thenReturn(new ExamAssignee());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> response = doGet("/api/assignee/1", httpEntity, String.class);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldCreatePersonalExamsOfGroupOnStudent() {
        doNothing().when(examService).createGroupExamsOnStudent(anyLong(), any());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        Student student = new Student();
        HttpEntity<Student> httpEntity = new HttpEntity<>(student, httpHeaders);
        ResponseEntity<String> response = doPost("/api/assignee/group/1", httpEntity, String.class);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldReturnExamReportModel() {
        when(examService.getReportInfo(1L)).thenReturn(new ExamReportModel());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<ExamReportModel> response = doGet("/api/assignee/1/report", httpEntity, ExamReportModel.class);
        assertEquals(200, response.getStatusCode().value());
    }
}