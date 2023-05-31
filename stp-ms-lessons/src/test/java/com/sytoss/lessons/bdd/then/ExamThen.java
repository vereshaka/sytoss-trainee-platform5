package com.sytoss.lessons.bdd.then;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.dto.ExamDTO;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ExamThen extends CucumberIntegrationTest {

    @Then("\"{word}\" exam should be from {word} to {word} with {int} tasks for \"{word}\" group with {int} minutes duration")
    public void examShouldBeWithParams(String examName, String relevantFromString, String relevantToString, Integer numberOfTasks, String groupName, Integer duration) throws ParseException {
        ExamDTO examDTO = getExamConnector().getByNameAndGroupName(examName, groupName);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date relevantFrom = dateFormat.parse(relevantFromString);
        Date relevantTo = dateFormat.parse(relevantToString);

        Assertions.assertEquals(examName, examDTO.getName());
        Assertions.assertEquals(relevantFrom, examDTO.getRelevantFrom());
        Assertions.assertEquals(relevantTo, examDTO.getRelevantTo());
        Assertions.assertEquals(numberOfTasks, examDTO.getNumberOfTasks());
        Assertions.assertEquals(duration, examDTO.getDuration());
    }

    @Then("\"{word}\" exam for \"{word}\" group should have topics")
    public void examShouldHaveTopic(String examName, String groupName, List<TopicDTO> topicDTOList) {
        ExamDTO examDTO = getExamConnector().getByNameAndGroupName(examName, groupName);

        Assertions.assertTrue(topicDTOList.stream()
                .allMatch(expectedTopic -> examDTO.getTopics().stream()
                        .anyMatch(actualTopic -> Objects.equals(actualTopic.getName(), expectedTopic.getName()) &&
                                Objects.equals(actualTopic.getDiscipline().getName(), expectedTopic.getDiscipline().getName())
                        )
                )
        );
    }
}
