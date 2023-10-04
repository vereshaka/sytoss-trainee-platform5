package com.sytoss.lessons.controllers;

import com.sytoss.lessons.connectors.ExamConnector;
import com.sytoss.lessons.connectors.TopicConnector;
import com.sytoss.lessons.connectors.UserConnector;
import com.sytoss.lessons.services.*;
import com.sytoss.stp.test.StpApplicationTest;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;

public class LessonsControllerTest extends StpApplicationTest {

    @InjectMocks
    protected DisciplineController disciplineController;

    @InjectMocks
    protected DisciplinesController disciplinesController;

    @InjectMocks
    protected ExamController examController;

    @InjectMocks
    protected TaskController taskController;

    @InjectMocks
    protected TaskDomainController taskDomainController;

    @InjectMocks
    protected TeacherController teacherController;

    @InjectMocks
    protected TopicController topicController;

    @MockBean
    protected TopicService topicService;

    @MockBean
    protected DisciplineService disciplineService;

    @MockBean
    protected TaskDomainService taskDomainService;

    @MockBean
    protected TaskService taskService;

    @MockBean
    protected ExamService examService;

    @MockBean
    protected UserConnector userConnector;

    @MockBean
    protected TopicConnector topicConnector;

    @MockBean
    protected ExamConnector examConnector;

    @MockBean
    protected ExamAssigneeService examAssigneeService;

    @InjectMocks
    protected ExamAssigneeController examAssigneeController;

    @Override
    protected String getToken() {
        return generateJWT(new ArrayList<>(), "John", "Johnson", "test@test.com", "teacher");
    }
}
