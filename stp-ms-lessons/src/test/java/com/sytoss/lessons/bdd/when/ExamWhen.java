package com.sytoss.lessons.bdd.when;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sytoss.domain.bom.lessons.*;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.bom.ExamModelForGroup;
import com.sytoss.lessons.dto.DisciplineDTO;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    @When("^a teacher create \"(.*)\" exam from (.*) to (.*) with (.*) tasks for this group in \"(.*)\" discipline with (.*) minutes duration")
    public void teacherCreateExamWithParams(String examName, String relevantFrom, String relevantTo, Integer numberOfTasks, String disciplineName, Integer duration, List<Topic> topics) throws ParseException {
        String url = "/api/exam/save";

        Teacher teacher = new Teacher();
        teacher.setId(getTestExecutionContext().getDetails().getTeacherId());

        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, getTestExecutionContext().getDetails().getTeacherId());
        Discipline discipline = new Discipline();
        discipline.setName(disciplineDTO.getName());
        discipline.setTeacher(teacher);

        Group group = new Group();
        group.setId(getTestExecutionContext().getDetails().getGroupReferenceId());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        for (Topic topic : topics) {
            topic.getDiscipline().setTeacher(teacher);
        }

        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < numberOfTasks; i++) {
            Task task = new Task();
            task.setTaskDomain(new TaskDomain());
            //  task.setTopics(topics);
        }

        Exam exam = new Exam();
        exam.setName(examName);
        //   exam.setGroup(group);
        //exam.setRelevantFrom(dateFormat.parse(relevantFrom));
       // exam.setRelevantTo(dateFormat.parse(relevantTo));
        exam.setNumberOfTasks(numberOfTasks);
        //exam.setDuration(duration);
        exam.setTopics(topics);
        exam.setDiscipline(discipline);
        exam.setTeacher(teacher);
        exam.setTasks(tasks);

        ExamModelForGroup examModelForGroup = new ExamModelForGroup();
        examModelForGroup.setExam(exam);
       // examModelForGroup.setGroups(List.of(exam.getGroup()));

        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<Exam> requestEntity = new HttpEntity<>(exam, httpHeaders);

        ResponseEntity<ExamModelForGroup> responseEntity = doPost(url, requestEntity, ExamModelForGroup.class);
        getTestExecutionContext().setResponse(responseEntity);
    }
}
