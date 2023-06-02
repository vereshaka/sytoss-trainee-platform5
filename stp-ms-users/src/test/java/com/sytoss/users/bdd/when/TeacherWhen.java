package com.sytoss.users.bdd.when;

import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.users.bdd.CucumberIntegrationTest;
import com.sytoss.users.bdd.common.TestExecutionContext;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

public class TeacherWhen extends CucumberIntegrationTest {

    @When("^anonymous register in system as teacher with \"(.*)\" firstname and \"(.*)\" middlename and \"(.*)\" lastname$")
    public void teacherCreating(String firstname, String middlename, String lastname) {
        String url = "/api/teacher/";
        Teacher teacher = new Teacher();
        teacher.setFirstName(firstname);
        teacher.setMiddleName(middlename);
        teacher.setLastName(lastname);
        ResponseEntity<Teacher> responseEntity = doPost(url, teacher, new ParameterizedTypeReference<Teacher>() {
        });
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}