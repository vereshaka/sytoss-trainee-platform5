package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.bdd.common.ExamAssigneeView;
import com.sytoss.lessons.bdd.common.ExamView;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamAssigneeDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import com.sytoss.stp.test.cucumber.TestExecutionContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;

import java.sql.Timestamp;
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

    @Given("^this discipline with id (.*) has exams$")
    public void examExists(String disciplineId,DataTable exams) {
        List<ExamView> examList = exams.asMaps(String.class, String.class).stream().toList().stream().map(ExamView::new).toList();
        for (ExamView item : examList) {
            ExamDTO dto = new ExamDTO();
            dto.setName(item.getName());
            dto.setMaxGrade(Integer.valueOf(item.getMaxGrade()));
            dto.setTasks(new ArrayList<>());
            dto.setTeacherId(getTestExecutionContext().getDetails().getTeacherId());
            Long id = (Long) getTestExecutionContext().replaceId(disciplineId);
            dto.setDiscipline(getDisciplineConnector().getReferenceById(id));
            dto.setTopics(getTopicConnector().findByDisciplineIdOrderByName(id));
            List<String> taskIds = Arrays.stream(item.getTasks().split(",")).map(String::trim).toList();
            for (String taskId : taskIds) {
                id = (Long) getTestExecutionContext().replaceId(taskId);
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

    @Given("^this exams have assignees$")
    public void examAssigneesExists(DataTable assignees) {
        List<ExamAssigneeView> examAssigneeViews = assignees.asMaps(String.class, String.class).stream().toList().stream().map(ExamAssigneeView::new).toList();
        for (ExamAssigneeView item : examAssigneeViews) {
            ExamAssigneeDTO dto = new ExamAssigneeDTO();
            dto.setRelevantFrom(Timestamp.valueOf(item.getRelevantFrom()));
            dto.setRelevantTo(Timestamp.valueOf(item.getRelevantTo()));
            Long id = (Long) getTestExecutionContext().replaceId(item.getExamId());
            dto.setExam(getExamConnector().getReferenceById(id));
            dto = getExamAssigneeConnector().save(dto);
            getTestExecutionContext().registerId(item.getId(), dto.getId());
        }
    }

    @Given("^this discipline has exams$")
    public void examExists(DataTable exams) {
        List<ExamView> examList = exams.asMaps(String.class, String.class).stream().toList().stream().map(ExamView::new).toList();
        for (ExamView item : examList) {
            ExamDTO dto = new ExamDTO();
            dto.setName(item.getName());
            dto.setMaxGrade(Integer.valueOf(item.getMaxGrade()));
            dto.setTasks(new ArrayList<>());
            dto.setDiscipline(getDisciplineConnector().getReferenceById(getTestExecutionContext().getDetails().getDisciplineId()));
            dto.setTeacherId(getTestExecutionContext().getDetails().getTeacherId());
            List<String> taskIds = Arrays.stream(item.getTasks().split(",")).map(String::trim).toList();
            for (String taskId : taskIds) {
                Long id = (Long)getTestExecutionContext().replaceId(taskId);
                dto.getTasks().add(getTaskConnector().getReferenceById(id));
            }
            dto = getExamConnector().save(dto);
            getTestExecutionContext().registerId(item.getId(), dto.getId());
        }
    }
}
