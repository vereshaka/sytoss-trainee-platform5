package com.sytoss.lessons.bdd.common;

import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.personalexam.Answer;
import com.sytoss.domain.bom.personalexam.AnswerStatus;
import com.sytoss.domain.bom.personalexam.PersonalExam;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.java.DataTableType;

import java.util.Map;

public class DataTableCommon {

    @DataTableType
    public DisciplineDTO mapDiscipline(Map<String, String> entry) {
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setName(entry.get("discipline"));

        if (entry.containsKey("teacherId")) {
            disciplineDTO.setTeacherId(Long.parseLong(entry.get("teacherId")));
        } else {
            disciplineDTO.setTeacherId(TestExecutionContext.getTestContext().getTeacherId());
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
    public GroupReferenceDTO mapGroups(Map<String, String> entry) {
        Long groupId = Long.parseLong(entry.get("group"));
        Long disciplineId = Long.parseLong(entry.get("discipline"));
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setId(disciplineId);
        return new GroupReferenceDTO(groupId,disciplineDTO);
    }

    @DataTableType
    public TaskDomainDTO mapTaskDomains(Map<String, String> entry) {
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        DisciplineDTO discipline = new DisciplineDTO();
        discipline.setName(entry.get("discipline"));
        taskDomainDTO.setName(entry.get("task domain"));
        taskDomainDTO.setDiscipline(discipline);
        return taskDomainDTO;
    }
}
