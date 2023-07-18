package com.sytoss.users.bdd.when;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.bdd.CucumberIntegrationTest;
import com.sytoss.users.bdd.common.TestExecutionContext;
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
public class UserWhen extends CucumberIntegrationTest {

    @When("^anonymous register in system as (teacher|student) with \"(.*)\" email and \"(\\w+)\" \"(\\w+)\" as name")
    public void teacherCreating(String userType, String email, String firstname, String lastname) throws JOSEException {
        String url = "/api/user/me";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(new ArrayList<>(), firstname, lastname, email, userType));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Teacher> responseEntity = doGet(url, httpEntity, Teacher.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(getToken());
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("firstName", TestExecutionContext.getTestContext().getUser().getFirstName());
        body.add("lastName", TestExecutionContext.getTestContext().getUser().getLastName());
        body.add("photo", new FileSystemResource(photoFile));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Void> responseEntity = getRestTemplate().postForEntity(getEndpoint("/api/user/me"), requestEntity, Void.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("student receive his groups")
    public void receiveAllGroupsOfStudent() {
        String url = "/api/user/me/groups";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<List<Group>> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<List<Group>>() {
        });
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("retrieve photo of this user")
    public void retrievePhotoOfThisUser() {
        String url = "/api/user/" + TestExecutionContext.getTestContext().getUser().getUid() + "/photo";
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<byte[]> responseEntity = doGet(url, httpEntity, new ParameterizedTypeReference<>() {
        });
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @When("^receive this student's photo")
    public void getUserPhoto() {
        String url = "/api/user/me/photo";
        HttpHeaders headers = getDefaultHttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> responseEntity = doGet(url, requestEntity, byte[].class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }

    @DataTableType
    public Discipline mapDiscipline(Map<String, String> row) {
        Discipline discipline = new Discipline();
        discipline.setName(row.get("discipline"));
        return discipline;
    }
}
