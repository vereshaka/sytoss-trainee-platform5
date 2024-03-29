package com.sytoss.users.bdd.when;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.bdd.UsersIntegrationTest;
import com.sytoss.users.dto.UserDTO;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
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
import java.util.Map;

@Transactional
public class UserWhen extends UsersIntegrationTest {

    @When("^anonymous register in system as (teacher|student) with \"(.*)\" email and \"(\\w+)\" \"(\\w+)\" as name")
    public void teacherCreating(String userType, String email, String firstname, String lastname) {
        String url = "/api/user/me";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(new ArrayList<>(), firstname, lastname, email, userType));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Teacher> responseEntity = doGet(url, httpEntity, Teacher.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^this user upload the photo$")
    public void updateStudentPhoto() {
        byte[] photoBytes = {0x01, 0x02, 0x03};
        File photoFile;
        try {
            photoFile = File.createTempFile("photo", ".jpg");
            Files.write(photoFile.toPath(), photoBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        UserDTO studentDTO = getTestExecutionContext().getDetails().getUser();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), studentDTO.getFirstName(), studentDTO.getLastName(), studentDTO.getEmail(), "s"));

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("firstName", getTestExecutionContext().getDetails().getUser().getFirstName());
        body.add("middleName", getTestExecutionContext().getDetails().getUser().getMiddleName());
        body.add("lastName", getTestExecutionContext().getDetails().getUser().getLastName());
        body.add("photo", new FileSystemResource(photoFile));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<Void> responseEntity = getRestTemplate().postForEntity(getEndpoint("/api/user/me"), requestEntity, Void.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("student receive his groups")
    public void receiveAllGroupsOfStudent() {
        String url = "/api/user/me/groups";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        UserDTO studentDTO = getTestExecutionContext().getDetails().getUser();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), studentDTO.getFirstName(), studentDTO.getLastName(), studentDTO.getEmail(), "s"));

        ResponseEntity<List<Group>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("retrieve photo of this user")
    public void retrievePhotoOfThisUser() {
        String url = "/api/user/" + getTestExecutionContext().getDetails().getUser().getUid() + "/photo";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        UserDTO studentDTO = getTestExecutionContext().getDetails().getUser();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), studentDTO.getFirstName(), studentDTO.getLastName(), studentDTO.getEmail(), "s"));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<byte[]> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^receive this student's photo")
    public void getUserPhoto() {
        String url = "/api/user/me/photo";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        UserDTO studentDTO = getTestExecutionContext().getDetails().getUser();
        httpHeaders.setBearerAuth(generateJWT(List.of("123"), studentDTO.getFirstName(), studentDTO.getLastName(), studentDTO.getEmail(), "s"));
        HttpEntity<?> requestEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<byte[]> responseEntity = doGet(url, requestEntity, byte[].class);
        getTestExecutionContext().setResponse(responseEntity);
    }
}
