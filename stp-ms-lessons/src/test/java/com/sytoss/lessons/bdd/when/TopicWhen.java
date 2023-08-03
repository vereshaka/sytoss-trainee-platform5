package com.sytoss.lessons.bdd.when;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.bom.TaskIds;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.java.en.When;
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
import java.util.List;

public class TopicWhen extends LessonsIntegrationTest {

    @When("^system retrieve information about topics by discipline$")
    public void requestSentCreatePersonalExam() {
        String url = "/api/discipline/" + getTestExecutionContext().getDetails().getDisciplineId() + "/topics";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Topic>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^retrieve information about topic by topicID$")
    public void getById() throws JOSEException {
        Long topicId = getTestExecutionContext().getDetails().getTopicId();
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        if (getTestExecutionContext().getDetails().getTopicId() == null) {
            topicId = 99L;
            String url = "/api/topic/" + topicId;
            ResponseEntity<String> responseEntity = doGet(url, httpEntity, String.class);
            getTestExecutionContext().setResponse(responseEntity);
        } else {
            String url = "/api/topic/" + topicId;
            ResponseEntity<Topic> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
            });
            getTestExecutionContext().setResponse(responseEntity);
        }
    }

    @When("^teacher create \"(.*)\" topic$")
    public void topicCreating(String topicName) {
        String url = "/api/discipline/" + getTestExecutionContext().getDetails().getDisciplineId() + "/topic";
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
        ResponseEntity<Topic> responseEntity = doPost(url, requestEntity, Topic.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^teacher creates existing \"(.*)\" topic$")
    public void existingTopicCreating(String topicName) {
        String url = "/api/discipline/" + getTestExecutionContext().getDetails().getDisciplineId() + "/topic";
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
        ResponseEntity<String> responseEntity = doPost(url, requestEntity, String.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^assign topic \"(.*)\" to this task$")
    public void linkTopicToThisTask(String topicName) {
        TopicDTO topicDTO = getTopicConnector().getByNameAndDisciplineId(topicName, getTestExecutionContext().getDetails().getDisciplineId());
        String url = "/api/topic/" + topicDTO.getId() + "/assign/tasks";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        TaskIds taskIds = new TaskIds();
        taskIds.setTaskIds(List.of(getTestExecutionContext().getDetails().getTaskId()));
        HttpEntity<?> httpEntity = new HttpEntity<>(taskIds, httpHeaders);
        ResponseEntity<List<Task>> responseEntity = doPost(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^receive this topic's icon$")
    public void getTopicIcon() {
        String url = "/api/topic/" + getTestExecutionContext().getDetails().getTopicId() + "/icon";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<byte[]> responseEntity = doGet(url, requestEntity, byte[].class);
        getTestExecutionContext().setResponse(responseEntity);
    }
}
