package com.sytoss.lessons.bdd.given;

import com.sytoss.domain.bom.lessons.ConditionType;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;

import java.util.List;
import java.util.Map;

public class TaskGiven extends CucumberIntegrationTest {

    @Given("^task with question \"(.*)\" exists$")
    public void taskExists(String question) {

        TaskDTO taskDTO = getTaskConnector().getByQuestionAndTopicsDisciplineId(question, TestExecutionContext.getTestContext().getDisciplineId());

        if (taskDTO == null) {
            TaskDomainDTO taskDomainDTO = getTaskDomainConnector().getReferenceById(TestExecutionContext.getTestContext().getTaskDomainId());
            TopicDTO topicDTO = getTopicConnector().getReferenceById(TestExecutionContext.getTestContext().getTopicId());
            taskDTO = new TaskDTO();
            taskDTO.setQuestion(question);
            taskDTO.setEtalonAnswer("Etalon answer");
            taskDTO.setTaskDomain(taskDomainDTO);
            taskDTO.setTopics(List.of(topicDTO));
            getTaskConnector().save(taskDTO);
        }
        TestExecutionContext.getTestContext().setTaskId(taskDTO.getId());
    }

    @Given("^task with question \"(.*)\" doesnt exist$")
    public void taskNotExist(String question) {
        TaskDTO taskDTO = getTaskConnector().getByQuestionAndTopicsDisciplineId(question, TestExecutionContext.getTestContext().getDisciplineId());
        if (taskDTO != null) {
            getTaskConnector().delete(taskDTO);
        }
    }

    @Given("tasks exist")
    public void tasksExist(DataTable tasks) {
        getTaskConnector().deleteAll();
        getTopicConnector().deleteAll();
        List<Map<String, String>> rows = tasks.asMaps();
        getListOfTasksFromDataTable(rows);
    }

    private void getListOfTasksFromDataTable(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            String disciplineName = columns.get("discipline");
            Long teacherId = TestExecutionContext.getTestContext().getTeacherId();
            DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, teacherId);
            if (disciplineDTO == null) {
                disciplineDTO = new DisciplineDTO();
                disciplineDTO.setName(disciplineName);
                disciplineDTO.setTeacherId(teacherId);
                disciplineDTO = getDisciplineConnector().save(disciplineDTO);
            }

            String topicName = columns.get("topic");
            TopicDTO topicDTO = getTopicConnector().getByNameAndDisciplineId(topicName, disciplineDTO.getId());
            if (topicDTO == null) {
                topicDTO = new TopicDTO();
                topicDTO.setName(topicName);
                topicDTO.setDiscipline(disciplineDTO);
                topicDTO = getTopicConnector().save(topicDTO);
            }
            TaskDTO taskDTO = getTaskConnector().getByQuestionAndTopicsDisciplineId(columns.get("task"), disciplineDTO.getId());
            if (taskDTO == null) {
                taskDTO = new TaskDTO();
                taskDTO.setQuestion(columns.get("task"));
                String conditionName = columns.get("condition");
                String type = columns.get("type");
                if (conditionName != null) {
                    TaskConditionDTO taskConditionDTO = getTaskConditionConnector().getByNameAndType(conditionName, ConditionType.valueOf(type));
                    if (taskConditionDTO == null) {
                        taskConditionDTO = new TaskConditionDTO();
                        taskConditionDTO.setName(conditionName);
                        taskConditionDTO.setType(ConditionType.CONTAINS);
                        taskConditionDTO = getTaskConditionConnector().save(taskConditionDTO);
                        taskDTO.getConditions().add(taskConditionDTO);
                    }else{
                        taskDTO.getConditions().add(taskConditionDTO);
                    }
                }
                taskDTO.setTaskDomain(getTaskDomainConnector().getReferenceById(TestExecutionContext.getTestContext().getTaskDomainId()));
                taskDTO.setTopics(List.of(topicDTO));
                taskDTO = getTaskConnector().save(taskDTO);
            } else {
                String conditionName = columns.get("condition");
                String type = columns.get("type");
                if (conditionName != null) {
                    TaskConditionDTO taskConditionDTO = getTaskConditionConnector().getByNameAndType(conditionName, ConditionType.valueOf(type));
                    if (taskConditionDTO == null) {
                        taskConditionDTO = new TaskConditionDTO();
                        taskConditionDTO.setName(conditionName);
                        taskConditionDTO.setType(ConditionType.CONTAINS);
                        taskConditionDTO = getTaskConditionConnector().save(taskConditionDTO);
                    }
                    if (!taskDTO.getConditions().contains(taskConditionDTO)) {
                        taskDTO.getConditions().add(taskConditionDTO);
                    }
                    taskDTO = getTaskConnector().save(taskDTO);
                }
            }
            TestExecutionContext.getTestContext().setTaskId(taskDTO.getId());
        }
    }
}