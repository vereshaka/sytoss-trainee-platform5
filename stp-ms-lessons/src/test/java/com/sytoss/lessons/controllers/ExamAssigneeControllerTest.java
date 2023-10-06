package com.sytoss.lessons.controllers;

import com.sytoss.lessons.dto.exam.assignees.ExamAssigneeDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamToStudentAssigneeDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ExamAssigneeControllerTest extends LessonsControllerTest{

    @Test
    void getListOfExamAssignee() {
        ExamDTO exam = new ExamDTO();
        exam.setId(1L);
        ExamAssigneeDTO examAssigneeDTO = new ExamAssigneeDTO();
        examAssigneeDTO.setId(1L);
        examAssigneeDTO.setExamAssigneeToDTOList(List.of(new ExamToStudentAssigneeDTO(),new ExamToStudentAssigneeDTO()));
        when(examConnector.getReferenceById(any())).thenReturn(exam);
        //exa;
    }

    @Test
    void getExamAssigneeById() {
    }
}