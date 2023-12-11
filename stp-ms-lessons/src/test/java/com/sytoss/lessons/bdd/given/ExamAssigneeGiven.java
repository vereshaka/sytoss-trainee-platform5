package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.bdd.common.ExamAssigneeToView;
import com.sytoss.lessons.dto.exam.assignees.ExamAssigneeDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamToGroupAssigneeDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamToStudentAssigneeDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;

import java.util.List;

public class ExamAssigneeGiven extends LessonsIntegrationTest {

    @Given("^this exam assignees have exam assignees to$")
    public void examAssigneesExists(DataTable assignees) {
        List<ExamAssigneeToView> examAssigneeToViews = assignees.asMaps(String.class, String.class).stream().toList().stream().map(ExamAssigneeToView::new).toList();
        for (ExamAssigneeToView item : examAssigneeToViews) {
            Long id = (Long) getTestExecutionContext().replaceId(item.getAssigneeId());
            ExamAssigneeDTO examAssigneeDTO = getExamAssigneeConnector().getReferenceById(id);
            if (item.getStudentId() != null) {
                ExamToStudentAssigneeDTO dto = new ExamToStudentAssigneeDTO();
                dto.setStudentId(Long.parseLong(item.getStudentId()));
                dto.setParent(examAssigneeDTO);
                dto = getExamAssigneeToConnector().save(dto);
            } else {
                ExamToGroupAssigneeDTO dto = new ExamToGroupAssigneeDTO();
                dto.setGroupId(Long.parseLong(item.getGroupId()));
                dto.setParent(examAssigneeDTO);
                dto = getExamAssigneeToConnector().save(dto);
            }
        }
    }
}
