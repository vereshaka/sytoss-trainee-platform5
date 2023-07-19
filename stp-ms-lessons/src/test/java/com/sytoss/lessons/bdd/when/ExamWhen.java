package com.sytoss.lessons.bdd.when;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class ExamWhen extends LessonsIntegrationTest {

    @DataTableType
    public Topic mapTopic(Map<String, String> entry) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(entry.get("discipline"), getTestExecutionContext().getDetails().getTeacherId());
        TopicDTO topicDTO = getTopicConnector().getByNameAndDisciplineId(entry.get("topic"), disciplineDTO.getId());

        Topic topic = new Topic();
        getTopicConvertor().fromDTO(topicDTO, topic);

        return topic;
    }

    @When("^a teacher create \"(.*)\" exam from (.*) to (.*) with (.*) tasks for \"(.*)\" group in \"(.*)\" discipline with (.*) minutes duration")
    public void teacherCreateExamWithParams(String examName, String relevantFrom, String relevantTo, Integer numberOfTasks, String groupName, String disciplineName, Integer duration, List<Topic> topics) throws ParseException, JOSEException {
        String url = "/api/exam/save";

        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, getTestExecutionContext().getDetails().getTeacherId());
        GroupReferenceDTO groupReferenceDTO = getGroupReferenceConnector().findByGroupId(getTestExecutionContext().getDetails().getGroupReferenceId());

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

        HttpHeaders httpHeaders = getDefaultHttpHeaders();
        HttpEntity<Exam> requestEntity = new HttpEntity<>(exam, httpHeaders);

        ResponseEntity<Exam> responseEntity = doPost(url, requestEntity, Exam.class);
        getTestExecutionContext().setResponse(responseEntity);
    }
}
