package com.sytoss.users.bdd.when;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.bdd.CucumberIntegrationTest;
import com.sytoss.users.bdd.common.TestExecutionContext;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
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
        byte[] photoBytes = { 0x01, 0x02, 0x03 };
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
        body.add("photo", new FileSystemResource(photoFile));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Void> responseEntity = getRestTemplate().postForEntity(getEndpoint("/api/user/updatePhoto"), requestEntity, Void.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}
