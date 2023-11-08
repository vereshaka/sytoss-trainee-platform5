package com.sytoss.lessons.bdd.then;

import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ExamThen extends LessonsIntegrationTest {

    @Then("^\"(.*)\" exam should have (.*) tasks for this group$")
    public void examShouldBeWithParams(String examName, Integer numberOfTasks) {
        ExamDTO examDTO = getExamConnector().getByName(examName);

        Assertions.assertEquals(examName, examDTO.getName());
        Assertions.assertEquals(numberOfTasks, examDTO.getNumberOfTasks());
    }

    @Then("^\"(.*)\" exam for this group should have topics$")
    public void examShouldHaveTopic(String examName, List<TopicDTO> topicDTOList) {
        ExamDTO examDTO = getExamConnector().getByName(examName);

        Assertions.assertTrue(topicDTOList.stream()
                .allMatch(expectedTopic -> examDTO.getTopics().stream()
                        .anyMatch(actualTopic -> Objects.equals(actualTopic.getName(), expectedTopic.getName()) &&
                                Objects.equals(actualTopic.getDiscipline().getName(), expectedTopic.getDiscipline().getName())
                        )
                )
        );
    }
}
