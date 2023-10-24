package com.sytoss.lessons.bdd.when;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sytoss.domain.bom.lessons.*;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.bom.ExamModelForGroup;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ExamWhen extends LessonsIntegrationTest {

    @When("^a teacher request exam list")
    public void teacherRequestExamList()  {
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity requestEntity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<List<Exam>> responseEntity = doGet("/api/teacher/my/exams", requestEntity, new ParameterizedTypeReference<>() {
        });
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^a teacher create exam by request (.*)")
    public void teacherCreateExamByRequest(String requestFileName) throws IOException {
        String url = "/api/exam/save";

        Teacher teacher = new Teacher();
        teacher.setId(getTestExecutionContext().getDetails().getTeacherId());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Exam request = objectMapper.readValue(getClass().getResourceAsStream("/data/" + requestFileName), Exam.class);

        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<Exam> requestEntity = new HttpEntity<>(request, httpHeaders);

        ResponseEntity<ExamModelForGroup> responseEntity = doPost(url, requestEntity, ExamModelForGroup.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^a teacher create \"(.*)\" exam with (.*) tasks for \"(.*)\" discipline")
    public void teacherCreateExamWithParams(String examName, Integer numberOfTasks, String disciplineName, List<Topic> topics) throws ParseException {
        String url = "/api/exam/save";

        Teacher teacher = new Teacher();
        teacher.setId(getTestExecutionContext().getDetails().getTeacherId());

        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, getTestExecutionContext().getDetails().getTeacherId());
        Discipline discipline = new Discipline();
        discipline.setId(disciplineDTO.getId());
        discipline.setName(disciplineDTO.getName());
        discipline.setTeacher(teacher);

        Group group = new Group();
        group.setId(getTestExecutionContext().getDetails().getGroupReferenceId());


        for (Topic topic : topics) {
            if (topic.getDiscipline().getName().equals(disciplineName)){
                topic.setId(getTopicConnector().getByNameAndDisciplineId(topic.getName(), disciplineDTO.getId()).getId());
                topic.setDiscipline(discipline);
            } else {
                throw new RuntimeException("Wrong test data");
            }
            topic.getDiscipline().setTeacher(teacher);
        }

        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < numberOfTasks; i++) {
            Task task = new Task();

            task.setTaskDomain(new TaskDomain());
        }

        Exam exam = new Exam();
        exam.setName(examName);
        exam.setNumberOfTasks(numberOfTasks);
        exam.setTopics(topics);
        exam.setDiscipline(discipline);
        exam.setTeacher(teacher);
        exam.setTasks(tasks);

        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<Exam> requestEntity = new HttpEntity<>(exam, httpHeaders);

        ResponseEntity<ExamModelForGroup> responseEntity = doPost(url, requestEntity, ExamModelForGroup.class);
        getTestExecutionContext().setResponse(responseEntity);
    }

    @When("^a teacher assign (.*) exam to groups: (.*)")
    public void teacherAssignExamToGroup(String examId, String groupIds){
        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        ExamAssignee assignee = new ExamAssignee();
        assignee.setRelevantFrom(new Date());
        assignee.setRelevantTo(new Date());
        assignee.setGroups(Arrays.stream(groupIds.split(",")).map(item -> {
            Group group = new Group();
            group.setId(Long.valueOf(item.trim()));
            return group;
        }).toList());
        HttpEntity<ExamAssignee> requestEntity = new HttpEntity<>(assignee, httpHeaders);

        ResponseEntity<ExamModelForGroup> responseEntity = doPost("/api/assignee/" +
                getTestExecutionContext().replaceId(examId) + "/groups",
                requestEntity, ExamModelForGroup.class);
        getTestExecutionContext().setResponse(responseEntity);
    }
}
