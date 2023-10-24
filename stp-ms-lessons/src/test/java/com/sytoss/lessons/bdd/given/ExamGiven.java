package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.bdd.common.ExamView;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExamGiven extends LessonsIntegrationTest {

    @Given("^\"(.*)\" discipline has group with id (.*)$")
    public void disciplineHasGroup(String disciplineName, Long groupId) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, getTestExecutionContext().getDetails().getTeacherId());
        GroupReferenceDTO groupReferenceDTO = new GroupReferenceDTO(groupId, disciplineDTO);
        getGroupReferenceConnector().save(groupReferenceDTO);
        getTestExecutionContext().getDetails().setGroupReferenceId(groupId);
    }

    @Given("^this discipline has exams$")
    public void examExists(DataTable exams) {
        List<ExamView> examList = exams.asMaps(String.class, String.class).stream().toList().stream().map(el -> new ExamView(el)).toList();
        for (ExamView item : examList) {
            ExamDTO dto = new ExamDTO();
            dto.setName(item.getName());
            dto.setMaxGrade(Integer.valueOf(item.getMaxGrade()));
            dto.setTasks(new ArrayList<>());
            dto.setTeacherId(getTestExecutionContext().getDetails().getTeacherId());
            List<String> taskIds = Arrays.stream(item.getTasks().split(",")).map(el -> el.trim()).toList();
            for (String taskId : taskIds) {
                Long id = (Long)getTestExecutionContext().replaceId(taskId);
                dto.getTasks().add(getTaskConnector().getReferenceById(id));
            }
            dto = getExamConnector().save(dto);
            getTestExecutionContext().registerId(item.getId(), dto.getId());
        }
    }

    @Given("^this discipline has assigned groups: (.*)")
    public void disciplineHasAssigneedGroups(String groupIds) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getReferenceById(getTestExecutionContext().getDetails().getDisciplineId());
        Arrays.stream(groupIds.split(",")).forEach(item -> {
            GroupReferenceDTO groupReferenceDTO = new GroupReferenceDTO(Long.valueOf(item.trim()), disciplineDTO);
            getGroupReferenceConnector().save(groupReferenceDTO);
        });
    }
}
