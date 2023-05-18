package com.sytoss.producer.bdd.then;

import com.sytoss.producer.bdd.CucumberIntegrationTest;
import com.sytoss.producer.bom.PersonalExam;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonalExamThen extends CucumberIntegrationTest {

    @Then("^status of \"(.*)\" exam for student with (.*) id should be \"(.*)\"$")
    public void personalExamShouldHaveStatus(String examName, String studentId, String personalExamStatus) {
        PersonalExam personalExam = getPersonalExamConnector().getByNameAndStudentId(examName, Long.parseLong(studentId));
        assertEquals(personalExamStatus, personalExam.getStatus().toString());
    }
}
