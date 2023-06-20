package com.sytoss.lessons.bdd.when;

import com.nimbusds.jose.JOSEException;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupDTO;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class ExamWhen extends CucumberIntegrationTest {

    @DataTableType
    public Topic mapTopic(Map<String, String> entry) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByName(entry.get("discipline"));
        TopicDTO topicDTO = getTopicConnector().getByNameAndDisciplineId(entry.get("topic"), disciplineDTO.getId());

        Topic topic = new Topic();
        getTopicConvertor().fromDTO(topicDTO, topic);

        return topic;
    }

    @When("a teacher create \"{word}\" exam from {word} to {word} with {int} tasks for \"{word}\" group in {word} discipline with {int} minutes duration")
    public void teacherCreateExamWithParams(String examName, String relevantFrom, String relevantTo, Integer numberOfTasks, String groupName, String disciplineName, Integer duration, List<Topic> topics) throws ParseException, JOSEException {
        String url = "/api/exam/save";

        DisciplineDTO disciplineDTO = getDisciplineConnector().getByName(disciplineName);
        GroupDTO groupDTO = getGroupConnector().getByNameAndDisciplineId(groupName, disciplineDTO.getId());

        Group group = new Group();
        getGroupConvertor().fromDTO(groupDTO, group);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        Exam exam = new Exam();
        exam.setName(examName);
        exam.setGroup(group);
        exam.setRelevantFrom(dateFormat.parse(relevantFrom));
        exam.setRelevantTo(dateFormat.parse(relevantTo));
        exam.setNumberOfTasks(numberOfTasks);
        exam.setDuration(duration);
        exam.setTopics(topics);

        HttpHeaders headers = getDefaultHttpHeaders();
        HttpEntity<Exam> requestEntity = new HttpEntity<>(exam, headers);

        ResponseEntity<Exam> responseEntity = doPost(url, requestEntity, Exam.class);
        TestExecutionContext.getTestContext().setResponse(responseEntity);
    }
}
