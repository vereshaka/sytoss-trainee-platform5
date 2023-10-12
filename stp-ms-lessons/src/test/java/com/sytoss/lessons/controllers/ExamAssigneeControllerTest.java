package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.lessons.dto.exam.assignees.ExamAssigneeDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamToStudentAssigneeDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class ExamAssigneeControllerTest extends LessonsControllerTest {

    @Test
    void getListOfExamAssignee() {
        ExamDTO exam = new ExamDTO();
        exam.setId(1L);
        ExamAssigneeDTO examAssigneeDTO = new ExamAssigneeDTO();
        examAssigneeDTO.setId(1L);
        examAssigneeDTO.setExamAssigneeToDTOList(List.of(new ExamToStudentAssigneeDTO(), new ExamToStudentAssigneeDTO()));
        when(examConnector.getReferenceById(any())).thenReturn(exam);
        //exa;
    }

    @Test
    void getExamAssigneeById() {
        LinkedHashMap<String, Object> user = new LinkedHashMap<>();
        user.put("id", 1);
        user.put("firstName", "John");
        user.put("lastName", "Doe");
        user.put("email", "john.doe@email.com");
        when(userConnector.getMyProfile()).thenReturn(user);
        when(examService.returnExamAssigneeById(1L)).thenReturn(new ExamAssignee());

        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> response = doGet("/api/assignee/1", httpEntity, String.class);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldCreatePersonalExamsOfGroupOnStudent() {
        LinkedHashMap<String, Object> user = new LinkedHashMap<>();
        user.put("id", 1);
        user.put("firstName", "John");
        user.put("lastName", "Doe");
        user.put("email", "john.doe@email.com");
        when(userConnector.getMyProfile()).thenReturn(user);
        doNothing().when(examService).createGroupExamsOnStudent(anyLong(), any());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        Student student = new Student();
        HttpEntity<Student> httpEntity = new HttpEntity<>(student, httpHeaders);
        ResponseEntity<String> response = doPost("/api/assignee/group/1", httpEntity, String.class);
        assertEquals(200, response.getStatusCode().value());
    }
}