package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.bdd.common.ExamView;
import com.sytoss.lessons.dto.TopicDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Then("exams should be")
    public void examsShouldBe(DataTable exams) {
        List<Exam> examsFromTheFeature = new ArrayList<>();
        List<ExamView> examList = exams.asMaps(String.class, String.class).stream().toList().stream().map(ExamView::new).toList();
        for (ExamView item : examList) {
            Exam exam = new Exam();
            exam.setId((Long) getTestExecutionContext().replaceId(item.getId()));
            exam.setName(item.getName());
            examsFromTheFeature.add(exam);
        }

        List<Exam> examsFromResponse = (List<Exam>) getTestExecutionContext().getResponse().getBody();
        assertEquals(examsFromTheFeature.size(), examsFromResponse.size());
        for (Exam examFromFeature : examsFromTheFeature) {
            List<Exam> filterResult = examsFromResponse.stream().filter(exam -> Objects.equals(examFromFeature.getId(), exam.getId()) &&
                    Objects.equals(examFromFeature.getName(), exam.getName())).toList();
            assertEquals(1, filterResult.size(), "Item# " + (examsFromTheFeature.size() - examsFromResponse.size()));
            examsFromResponse.remove(filterResult.get(0));
        }
    }
}
