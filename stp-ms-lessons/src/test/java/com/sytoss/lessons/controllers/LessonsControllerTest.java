package com.sytoss.lessons.controllers;

import com.sytoss.lessons.connectors.TopicConnector;
import com.sytoss.lessons.connectors.UserConnector;
import com.sytoss.lessons.services.*;
import com.sytoss.stp.test.StpApplicationTest;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;

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

    @Override
    protected String getToken() {
        return generateJWT(new ArrayList<>(), "John", "Johnson", "test@test.com", "teacher");
    }
}
