package com.sytoss.lessons.bdd.common;

import com.sytoss.domain.bom.checktask.QueryResult;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.AnswerStatus;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.dto.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import liquibase.util.StringUtil;

import java.util.*;

public class DataTableCommon extends LessonsIntegrationTest {

    @DataTableType
    public DisciplineDTO mapDiscipline(Map<String, String> entry) {
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setName(entry.get("discipline"));

        if (entry.containsKey("teacherId")) {
            disciplineDTO.setTeacherId(Long.parseLong(entry.get("teacherId")));
        } else {
            disciplineDTO.setTeacherId(getTestExecutionContext().getDetails().getTeacherId());
        }

        return disciplineDTO;
    }

    @DataTableType
    public PersonalExam mapPersonalExam(Map<String, String> entry) {
        PersonalExam personalExam = new PersonalExam();
        personalExam.setName(entry.get("examName"));
        Task task = new Task();
        TaskDomain taskDomain = new TaskDomain();
        taskDomain.setName(entry.get("task domain"));
        task.setTaskDomain(taskDomain);
        Answer answer = new Answer();
        answer.setTask(task);
        answer.setStatus(AnswerStatus.NOT_STARTED);
        personalExam.getAnswers().add(answer);
        return personalExam;
    }

    @DataTableType
    public TopicDTO mapTopic(Map<String, String> entry) {
        TopicDTO topic = new TopicDTO();
        DisciplineDTO discipline = new DisciplineDTO();
        topic.setName(entry.get("topic"));
        discipline.setName(entry.get("discipline"));
        topic.setDiscipline(discipline);
        return topic;
    }

    @DataTableType
    public GroupReferenceDTO mapGroupsDTO(Map<String, String> entry) {
        Long groupId = Long.parseLong(entry.get("group"));
        Long disciplineId = Long.parseLong(entry.get("discipline"));
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setId(disciplineId);
        return new GroupReferenceDTO(groupId, disciplineDTO);
    }

    @DataTableType
    public TaskDomainDTO mapTaskDomains(Map<String, String> entry) {
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        DisciplineDTO discipline = new DisciplineDTO();
        discipline.setName(entry.get("discipline"));
        taskDomainDTO.setName(entry.get("task domain"));
        taskDomainDTO.setDiscipline(discipline);
        String id = entry.get("id");
        if (id != null) {
            taskDomainDTO.setId(Long.parseLong(id.substring(1)));
        }
        return taskDomainDTO;
    }

    @DataTableType
    public Group mapGroups(Map<String, String> entry) {
        Long groupId = Long.parseLong(entry.get("group"));
        Group group = new Group();
        group.setId(groupId);
        return group;
    }

    @DataTableType
    public TaskDTO mapTasksDTO(Map<String, String> entry) {
        String task = entry.get("task");
        String taskDomainStringId = entry.get("taskDomainId");
        Long taskDomainId = getTestExecutionContext().getIdMapping().get(taskDomainStringId);
        TaskDTO task1 = new TaskDTO();
        task1.setQuestion(task);
        TaskDomainDTO taskDomain = getTestExecutionContext().getDetails().getTaskDomains().stream().filter(el -> Objects.equals(el.getId(), taskDomainId)).toList().get(0);
        task1.setTaskDomain(taskDomain);
        return task1;
    }

    @DataTableType
    public QueryResult mapQueryResult(DataTable table) {
        List<Map<String, String>> tableMaps = table.entries();
        List<Map<String, Object>> resultMaps = new ArrayList<>();
        for(Map<String, String> tableMap: tableMaps){
            Map<String, Object> resultMap = new HashMap<>();
            for(String key : tableMap.keySet().stream().toList()){
                resultMap.put(key, StringUtil.isNumeric(tableMap.get(key)) ? Integer.parseInt(tableMap.get(key)) : tableMap.get(key));
            }
            resultMaps.add(resultMap);
        }
        QueryResult queryResult = new QueryResult();

        List<String> header = tableMaps.get(0).keySet().stream().toList();
        queryResult.setHeader(header);
        for (Map<String, Object> row : resultMaps) {
            queryResult.addValues(row);
        }

        return queryResult;
    }
}
