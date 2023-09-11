package com.sytoss.lessons.bdd.when;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.bom.ExamModel;
import com.sytoss.lessons.dto.DisciplineDTO;
import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ExamWhen extends LessonsIntegrationTest {

    /*  @DataTableType
      public Topic mapTopic(Map<String, String> entry) {
          DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(entry.get("discipline"), getTestExecutionContext().getDetails().getTeacherId());
          TopicDTO topicDTO = getTopicConnector().getByNameAndDisciplineId(entry.get("topic"), disciplineDTO.getId());

          Topic topic = new Topic();
          getTopicConvertor().fromDTO(topicDTO, topic);

          return topic;
      }
  */
    @When("^a teacher create \"(.*)\" exam from (.*) to (.*) with (.*) tasks for \"(.*)\" group in \"(.*)\" discipline with (.*) minutes duration")
    public void teacherCreateExamWithParams(String examName, String relevantFrom, String relevantTo, Integer numberOfTasks, String disciplineName, Integer duration, List<Topic> topics) throws ParseException, JOSEException {
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

        Exam exam = new Exam();
        exam.setName(examName);
        exam.setGroup(group);
        exam.setRelevantFrom(dateFormat.parse(relevantFrom));
        exam.setRelevantTo(dateFormat.parse(relevantTo));
        exam.setNumberOfTasks(numberOfTasks);
        exam.setDuration(duration);
        exam.setTopics(topics);
        exam.setDiscipline(discipline);
        exam.setTeacher(teacher);
        exam.setTasks(List.of(new Task(), new Task(), new Task(), new Task(), new Task()));

        ExamModel examModel = new ExamModel();
        examModel.setExam(exam);
        examModel.setGroups(List.of(exam.getGroup()));

        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<ExamModel> requestEntity = new HttpEntity<>(examModel, httpHeaders);

        ResponseEntity<ExamModel> responseEntity = doPost(url, requestEntity, ExamModel.class);
        getTestExecutionContext().setResponse(responseEntity);
    }
}
