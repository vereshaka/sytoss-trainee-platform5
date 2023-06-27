package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.bson.assertions.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;


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
        for (DisciplineDTO disciplineDTO: disciplines) {
            List<Discipline> foundTDisciplines = disciplineList.stream().filter(item ->
                    item.getName().equals(disciplineDTO.getName()) &&
                            item.getTeacher().getId().equals(disciplineDTO.getTeacherId())).toList();
            if (foundTDisciplines.size() == 0) {
                fail("Discipline with name " + disciplineDTO.getName() + " and teacherId " + disciplineDTO.getTeacherId() + " not found");
            }
            disciplineList.remove(foundTDisciplines.get(0));
        }
        assertEquals(0, disciplineList.size());
    }

    @Then("^discipline's icon should be received$")
    public void disciplineIconShouldBeReceived() {
        Optional<DisciplineDTO> optionalDisciplineDTO = getDisciplineConnector().findById(TestExecutionContext.getTestContext().getDisciplineId());
        DisciplineDTO disciplineDTO = optionalDisciplineDTO.orElse(null);
        byte[] icon = (byte[]) TestExecutionContext.getTestContext().getResponse().getBody();
        assertEquals(disciplineDTO.getIcon().length, icon.length);
    }
}
