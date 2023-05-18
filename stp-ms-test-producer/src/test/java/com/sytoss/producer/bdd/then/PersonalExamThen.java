package com.sytoss.producer.bdd.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sytoss.producer.bdd.CucumberIntegrationTest;

import com.sytoss.producer.bdd.common.IntegrationTest;
import com.sytoss.producer.bom.PersonalExam;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;

import java.util.List;
import java.util.Map;

public class PersonalExamThen extends CucumberIntegrationTest {

    @Given("^\"(.*)\" exam for sudent with 1L id should have tasks$")
    public void thisCustomerHasProjects(String examName, DataTable table) throws JsonProcessingException {
        List<Map<String, String>> rows = table.asMaps();
        //TODO dmitriyK: need to rewrite when metadata microservice would be exist
        getMapper().readValue(IntegrationTest.getTestContext().getResponse().getBody(), PersonalExam.class);
    }
}
