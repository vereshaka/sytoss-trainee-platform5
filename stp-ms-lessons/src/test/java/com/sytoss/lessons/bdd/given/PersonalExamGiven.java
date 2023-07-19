package com.sytoss.lessons.bdd.given;

import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.personalexam.PersonalExamStatus;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import io.cucumber.java.en.Given;

import java.util.List;

public class PersonalExamGiven extends LessonsIntegrationTest {

    @Given("^personal exam exists$")
    public void personalExist(List<PersonalExam> personalExams) {
        for(PersonalExam personalExam : personalExams){
            personalExam.setStatus(PersonalExamStatus.NOT_STARTED);
            for(Answer answer : personalExam.getAnswers()){
                DisciplineDTO disciplineDTO = getDisciplineConnector().getReferenceById(getTestExecutionContext().getDetails().getDisciplineId());
                TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getByNameAndDisciplineId(answer.getTask().getTaskDomain().getName(), disciplineDTO.getId());
                if(taskDomainDTO == null){
                    taskDomainDTO = new TaskDomainDTO();
                    taskDomainDTO.setName(answer.getTask().getTaskDomain().getName());
                    taskDomainDTO.setDiscipline(disciplineDTO);
                }
                taskDomainDTO = getTaskDomainConnector().save(taskDomainDTO);
                TaskDomain taskDomain = new TaskDomain();
                getTaskDomainConvertor().fromDTO(taskDomainDTO, taskDomain);
                answer.getTask().setTaskDomain(taskDomain);
            }
        }
        getTestExecutionContext().getDetails().setPersonalExams(personalExams);
    }
}
