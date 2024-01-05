package com.sytoss.lessons.bdd.then;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.bdd.common.ExamView;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TopicDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Then("exam is")
    public void examIs(DataTable examTable) {
        ExamView examView = examTable.asMaps(String.class, String.class).stream().toList().stream().map(ExamView::new).toList().get(0);

        Exam exam = new Exam();
        exam.setId((long) getTestExecutionContext().replaceId(examView.getId()));
        exam.setName(examView.getName());
        exam.setMaxGrade(Integer.valueOf(examView.getMaxGrade()));
        exam.setNumberOfTasks(Integer.valueOf(examView.getTaskCount()));
        exam.setTasks(new ArrayList<>());
        Discipline discipline = new Discipline();
        discipline.setId(getTestExecutionContext().getDetails().getDisciplineId());
        exam.setDiscipline(discipline);
        Teacher teacher = new Teacher();
        teacher.setId(getTestExecutionContext().getDetails().getTeacherId());
        exam.setTeacher(teacher);
        List<String> taskIds = Arrays.stream(examView.getTasks().split(",")).map(String::trim).toList();
        List<Topic> topics = new ArrayList<>();
        for (String taskId : taskIds) {
            Long id = (Long) getTestExecutionContext().replaceId(taskId);
            TaskDTO taskDTO = getTaskConnector().findById(id).orElse(null);
            if (taskDTO != null) {
                Task task = new Task();
                getTaskConvertor().fromDTO(taskDTO, task);
                exam.getTasks().add(task);
                for (Topic topic : task.getTopics()) {
                    if (!topics.stream().map(Topic::getId).toList().contains(topic.getId())) {
                        topics.add(topic);
                    }
                }
            }

        }
        exam.setTopics(topics);
        if (!topics.isEmpty()) {
            exam.setDiscipline(topics.get(0).getDiscipline());
        }

        Exam examFromResponse = (Exam) getTestExecutionContext().getResponse().getBody();
        assertEquals(exam.getId(), examFromResponse.getId());
        assertEquals(exam.getName(), examFromResponse.getName());
        assertEquals(exam.getNumberOfTasks(), examFromResponse.getNumberOfTasks());
        assertEquals(exam.getMaxGrade(), examFromResponse.getMaxGrade());
        assertEquals(exam.getTasks().size(), examFromResponse.getTasks().size());
        assertEquals(exam.getTopics().size(), examFromResponse.getTopics().size());
        assertEquals(exam.getDiscipline().getId(), examFromResponse.getDiscipline().getId());
        assertEquals(exam.getTeacher().getId(), examFromResponse.getTeacher().getId());
    }
}
