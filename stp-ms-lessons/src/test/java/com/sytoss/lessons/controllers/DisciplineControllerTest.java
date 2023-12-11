package com.sytoss.lessons.controllers;

import com.sytoss.domain.bom.exceptions.business.DisciplineExistException;
import com.sytoss.domain.bom.exceptions.business.notfound.DisciplineNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.dto.GroupsIds;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class DisciplineControllerTest extends LessonsControllerTest {

    @Test
    public void shouldSaveTopic() {
        LinkedHashMap<String, Object> teacherMap = new LinkedHashMap<>();
        teacherMap.put("id", 1);
        when(userConnector.getMyProfile()).thenReturn(teacherMap);
        when(topicService.create(anyLong(), any(Topic.class))).thenReturn(new Topic());

        byte[] photoBytes = {0x01, 0x02, 0x03};
        File photoFile;
        try {
            photoFile = File.createTempFile("photo", ".jpg");
            Files.write(photoFile.toPath(), photoBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("name", "anything");
        body.add("icon", new FileSystemResource(photoFile));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<Topic> result = doPost("/api/discipline/1/topic", requestEntity, Topic.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldFindGroupsByDiscipline() {
        when(disciplineService.getGroups(any())).thenReturn(new ArrayList<>());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<List<Group>> result = doGet("/api/discipline/123/groups", requestEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldSaveDiscipline() {
        when(disciplineService.create(any(Discipline.class))).thenReturn(new Discipline());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("name", "discipline1");

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<Discipline> result = doPost("/api/discipline", httpEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    void shouldReturnExceptionWhenSaveExistingDiscipline() {
        when(disciplineService.create(any(Discipline.class))).thenThrow(new DisciplineExistException("SQL"));

        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("name", "discipline1");

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<String> result = doPost("/api/discipline", httpEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(409, result.getStatusCode().value());
    }

    @Test
    public void shouldGetDisciplineById() {
        when(disciplineService.getById(any())).thenReturn(new Discipline());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Discipline> result = doGet("/api/discipline/123", httpEntity, Discipline.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldNotGetDisciplineByIdWhenItDoesNotExist() {
        when(disciplineService.getById(any())).thenThrow(new DisciplineNotFoundException(123L));
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> result = doGet("/api/discipline/123", httpEntity, String.class);
        assertEquals(404, result.getStatusCode().value());
        //assertEquals("Discipline with id \"123\" not found", result.getBody());
        // TODO: 05.07.2023 LarinI: Find a way to check an error message
    }

    @Test
    public void shouldFindTasksDomainByDiscipline() {
        when(taskDomainService.findByDiscipline(any())).thenReturn(new ArrayList<>());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<List<TaskDomain>> result = doGet("/api/discipline/1/task-domains", requestEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }


    @Test
    public void shouldLinkGroupToDiscipline() {
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Void> result = doPost("/api/discipline/2/group/5", httpEntity, Void.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldReturnDisciplineIcon() {
        byte[] iconBytes = {0x01, 0x02, 0x03};
        when(disciplineService.getIcon(anyLong())).thenReturn(iconBytes);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<byte[]> result = doGet("/api/discipline/4/icon", httpEntity, byte[].class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldReturnListOfTopics() {
        when(topicService.findByDiscipline(any())).thenReturn(new ArrayList<>());
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Topic>> result = doGet("/api/discipline/1/topics", httpEntity, new ParameterizedTypeReference<>() {
        });
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    void assignGroupsToDiscipline() {
        when(topicService.findByDiscipline(any())).thenReturn(new ArrayList<>());
        LinkedHashMap<String, Object> teacherMap = new LinkedHashMap<>();
        teacherMap.put("id", 1);
        when(userConnector.getMyProfile()).thenReturn(teacherMap);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        GroupsIds groupsIds = new GroupsIds();
        groupsIds.setGroupsIds(List.of(1L, 2L));
        HttpEntity<?> httpEntity = new HttpEntity<>(groupsIds, httpHeaders);

        ResponseEntity<Void> result = doPost("/api/discipline/1/assign/groups", httpEntity, Void.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldDeleteDiscipline() {
        Discipline discipline = new Discipline();
        discipline.setId(1L);
        discipline.setName("SQL");
        discipline.setTeacher(new Teacher());
        when(disciplineService.getById(any())).thenReturn(discipline);
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<Discipline> result = doDelete("/api/discipline/1/delete", requestEntity, Discipline.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void shouldGetExamsByStudent() {
        Discipline discipline = new Discipline();
        discipline.setId(1L);
        discipline.setName("SQL");
        discipline.setTeacher(new Teacher());
        when(disciplineService.getExamsByStudent(any())).thenReturn(List.of(new Exam()));
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(new ArrayList<>(), "John", "Johnson", "test@test.com", "Student"));
        HttpEntity<?> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<List<Exam>> result = doGet("/api/discipline/1/student/exams", requestEntity, new ParameterizedTypeReference<>() {});
        assertEquals(200, result.getStatusCode().value());
    }
}
