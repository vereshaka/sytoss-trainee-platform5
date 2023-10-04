package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.notfound.ExamNotFoundException;
import com.sytoss.domain.bom.lessons.*;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.lessons.examassignee.ExamGroupAssignee;
import com.sytoss.domain.bom.lessons.examassignee.ExamStudentAssignee;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.connectors.ExamAssigneeConnector;
import com.sytoss.lessons.connectors.ExamConnector;
import com.sytoss.lessons.connectors.PersonalExamConnector;
import com.sytoss.lessons.connectors.UserConnector;
import com.sytoss.lessons.controllers.ExamAssigneeController;
import com.sytoss.lessons.convertors.*;
import com.sytoss.lessons.dto.*;
import com.sytoss.lessons.dto.exam.assignees.ExamAssigneeDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamToStudentAssigneeDTO;
import com.sytoss.stp.test.FileUtils;
import com.sytoss.stp.test.StpUnitTest;
import liquibase.statement.StoredProcedureStatement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ExamAssigneeServiceTest extends StpUnitTest {

    @InjectMocks
    private ExamAssigneeService examAssigneeService;

    @Mock
    private ExamConnector examConnector;

    @Mock
    private ExamAssigneeConnector examAssigneeConnector;

    @Spy
    private ExamConvertor examConvertor = new ExamConvertor(new TopicConvertor(new DisciplineConvertor()),
            new TaskConvertor(new TaskDomainConvertor(new DisciplineConvertor()), new TaskConditionConvertor(), new TopicConvertor(new DisciplineConvertor())), new ExamAssigneeConvertor());

    @Spy
    private ExamAssigneeConvertor examAssigneeConvertor = new ExamAssigneeConvertor();

    @Test
    void getListOfExamAssignee() {
        ExamDTO exam = createExamDTO();
        when(examConnector.getReferenceById(any())).thenReturn(exam);
        List<ExamAssignee> examAssignees = examAssigneeService.returnExamAssignees(1L);
        assertEquals(exam.getExamAssigneeDTOS().size(),examAssignees.size());
        assertEquals(exam.getExamAssigneeDTOS().get(0).getId(),examAssignees.get(0).getId());
    }

    @Test
    void getExamAssigneeById() {
        ExamDTO exam = createExamDTO();
        ExamAssigneeDTO expectedExamAssigneeDTO = exam.getExamAssigneeDTOS().get(0);
        ExamAssignee expectedExamAssignee = new ExamStudentAssignee();
        examAssigneeConvertor.fromDTO(expectedExamAssigneeDTO,expectedExamAssignee);
        when(examAssigneeConnector.getReferenceById(any())).thenReturn(exam.getExamAssigneeDTOS().get(0));
        ExamAssignee examAssigneeActual = examAssigneeService.returnExamAssigneeById(1L);
        assertEquals(expectedExamAssignee.getId(),examAssigneeActual.getId());
    }

    private ExamDTO createExamDTO(){
        ExamDTO exam = new ExamDTO();
        exam.setId(1L);
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setId(1L);
        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setDiscipline(disciplineDTO);
        exam.setTopics(List.of(topicDTO));
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTopics(List.of(topicDTO));
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainDTO.setId(1L);
        taskDomainDTO.setDiscipline(disciplineDTO);
        taskDomainDTO.setDataScript(FileUtils.readFromFile("puml/data.puml"));
        taskDomainDTO.setDatabaseScript(FileUtils.readFromFile("puml/database.puml"));
        taskDTO.setTaskDomain(taskDomainDTO);
        exam.setTasks(List.of(taskDTO));
        ExamAssigneeDTO examAssigneeDTO = new ExamAssigneeDTO();
        examAssigneeDTO.setId(1L);
        examAssigneeDTO.setExamAssigneeToDTOList(List.of(new ExamToStudentAssigneeDTO(),new ExamToStudentAssigneeDTO()));
        exam.getExamAssigneeDTOS().add(examAssigneeDTO);
        return exam;
    }

    @Test
    private void reschedule() {
        //todo IvanL do reschedule test
    }
}
