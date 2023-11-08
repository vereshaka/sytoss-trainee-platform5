package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.bdd.common.ExamAssigneeToView;
import com.sytoss.lessons.bdd.common.ExamAssigneeView;
import com.sytoss.lessons.bdd.common.ExamView;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamAssigneeDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamAssigneeToDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamToStudentAssigneeDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ExamAssigneeGiven extends LessonsIntegrationTest {

    @Given("^this exam assignees have exam assignees to$")
    public void examAssigneesExists(DataTable assignees) {
        List<ExamAssigneeToView> examAssigneeToViews = assignees.asMaps(String.class, String.class).stream().toList().stream().map(ExamAssigneeToView::new).toList();
        for (ExamAssigneeToView item : examAssigneeToViews) {
            ExamToStudentAssigneeDTO dto = new ExamToStudentAssigneeDTO();
            Long id = (Long) getTestExecutionContext().replaceId(item.getAssigneeId());
            ExamAssigneeDTO examAssigneeDTO = getExamAssigneeConnector().getReferenceById(id);
            dto.setParent(examAssigneeDTO);
            dto.setStudentId(Long.parseLong(item.getStudentId()));
            dto = getExamAssigneeToConnector().save(dto);
        }
    }
}
