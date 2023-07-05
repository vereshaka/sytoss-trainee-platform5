package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class DisciplineThen extends CucumberIntegrationTest {

    @Then("^\"(.*)\" discipline should be received$")
    public void disciplineShouldBeReceived(String disciplineName) {
        Discipline discipline = (Discipline) TestExecutionContext.getTestContext().getResponse().getBody();
        assertEquals(disciplineName, discipline.getName());
    }

    @Then("^\"(.*)\" discipline should exist$")
    public void disciplineShouldExist(String disciplineName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, TestExecutionContext.getTestContext().getTeacherId());
        Assertions.assertEquals(disciplineName, disciplineDTO.getName());
    }

    @Then("^disciplines should be received$")
    public void disciplinesShouldBeReceived(List<DisciplineDTO> disciplines) {
        List<Discipline> disciplineList = (List<Discipline>) TestExecutionContext.getTestContext().getResponse().getBody();
        assertEquals(disciplines.size(), disciplineList.size());
        for (int i = 0; i < disciplineList.size(); i++) {
            assertEquals(disciplines.get(i).getName(), disciplineList.get(i).getName());
        }
    }

    @Then("^discipline's icon should be received$")
    public void disciplineIconShouldBeReceived() {
        DisciplineDTO disciplineDTO = getDisciplineConnector().findById(TestExecutionContext.getTestContext().getDisciplineId()).orElse(null);
        byte[] icon = (byte[]) TestExecutionContext.getTestContext().getResponse().getBody();
        assertEquals(disciplineDTO.getIcon().length, icon.length);
    }

    @Then("should receive information about discipline of student")
    public void shouldReceiveInformationAboutDisciplineOfStudent(List<DisciplineDTO> disciplines) {
        List<Discipline> disciplineList = (List<Discipline>) TestExecutionContext.getTestContext().getResponse().getBody();
        assertNotNull(disciplineList.size());
        int count = 0;
        for (DisciplineDTO discipline : disciplines) {
            for (Discipline answer : disciplineList) {
                if (discipline.getName().equals(answer.getName())) {
                    count++;
                }
            }
        }
        assertEquals(disciplines.size(), count);
    }
}
