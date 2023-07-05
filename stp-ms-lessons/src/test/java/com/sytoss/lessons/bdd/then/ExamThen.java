package com.sytoss.lessons.bdd.then;

import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
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

    @Then("^\"(.*)\" exam should be from (.*) to (.*) with (.*) tasks for this group with (.*) minutes duration$")
    public void examShouldBeWithParams(String examName, String relevantFromString, String relevantToString, Integer numberOfTasks, Integer duration) throws ParseException {
        ExamDTO examDTO = getExamConnector().getByNameAndGroupId(examName, TestExecutionContext.getTestContext().getGroupReferenceId());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date relevantFrom = dateFormat.parse(relevantFromString);
        Date relevantTo = dateFormat.parse(relevantToString);

        Assertions.assertEquals(examName, examDTO.getName());
        Assertions.assertEquals(relevantFrom, examDTO.getRelevantFrom());
        Assertions.assertEquals(relevantTo, examDTO.getRelevantTo());
        Assertions.assertEquals(numberOfTasks, examDTO.getNumberOfTasks());
        Assertions.assertEquals(duration, examDTO.getDuration());
    }

    @Then("^\"(.*)\" exam for this group should have topics$")
    public void examShouldHaveTopic(String examName, List<TopicDTO> topicDTOList) {
        ExamDTO examDTO = getExamConnector().getByNameAndGroupId(examName, TestExecutionContext.getTestContext().getGroupReferenceId());

        Assertions.assertTrue(topicDTOList.stream()
                .allMatch(expectedTopic -> examDTO.getTopics().stream()
                        .anyMatch(actualTopic -> Objects.equals(actualTopic.getName(), expectedTopic.getName()) &&
                                Objects.equals(actualTopic.getDiscipline().getName(), expectedTopic.getDiscipline().getName())
                        )
                )
        );
    }
}
