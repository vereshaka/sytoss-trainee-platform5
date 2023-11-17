package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.controllers.api.ResponseObject;
import com.sytoss.lessons.dto.DisciplineDTO;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class DisciplineThen extends LessonsIntegrationTest {

    @Then("^\"(.*)\" discipline should be received$")
    public void disciplineShouldBeReceived(String disciplineName) {
        Discipline discipline = (Discipline) getTestExecutionContext().getResponse().getBody();
        assertEquals(disciplineName, discipline.getName());
    }

    @Then("^\"(.*)\" discipline should exist$")
    public void disciplineShouldExist(String disciplineName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, getTestExecutionContext().getDetails().getTeacherId());
        Assertions.assertEquals(disciplineName, disciplineDTO.getName());
    }

    @Then("^disciplines should be received$")
    public void disciplinesShouldBeReceived(List<Discipline> disciplines) {
        List<Discipline> disciplineList;
        if (getTestExecutionContext().getResponse().getBody() instanceof ArrayList) {
            disciplineList = (List<Discipline>) getTestExecutionContext().getResponse().getBody();
        } else {
            ResponseObject responseObject = (ResponseObject) getTestExecutionContext().getResponse().getBody();
            disciplineList = responseObject.getData();
        }
        assertEquals(disciplines.size(), disciplineList.size());
        assertTrue(
                disciplines.stream().allMatch(discipline -> disciplineList.stream().anyMatch(
                        backendDiscipline -> backendDiscipline.getName().equals(discipline.getName()))
                )
        );
    }

    @Then("^discipline's icon should be received$")
    public void disciplineIconShouldBeReceived() {
        DisciplineDTO disciplineDTO = getDisciplineConnector().findById(getTestExecutionContext().getDetails().getDisciplineId()).orElse(null);
        byte[] icon = (byte[]) getTestExecutionContext().getResponse().getBody();
        assertEquals(disciplineDTO.getIcon().length, icon.length);
    }

    @Then("should receive information about discipline of student")
    public void shouldReceiveInformationAboutDisciplineOfStudent(List<Discipline> disciplines) {
        List<Discipline> disciplineList = (List<Discipline>) getTestExecutionContext().getResponse().getBody();
        assertNotNull(disciplineList.size());
        int count = 0;
        for (Discipline discipline : disciplines) {
            for (Discipline answer : disciplineList) {
                if (discipline.getName().equals(answer.getName())) {
                    count++;
                }
            }
        }
        assertEquals(disciplines.size(), count);
    }
}
