package com.sytoss.users.bdd.when;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.bdd.CucumberIntegrationTest;
import com.sytoss.users.bdd.common.TestExecutionContext;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class TeacherWhen extends CucumberIntegrationTest {

    @When("^anonymous register in system as teacher with \"(.*)\" firstname and \"(.*)\" middlename and \"(.*)\" lastname$")
    public void teacherCreating(String firstname, String middlename, String lastname) throws JOSEException {
        String url = "/api/user/me";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(generateJWT(List.of("123")));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Teacher> responseEntity = doGet(url, httpEntity, Teacher.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}