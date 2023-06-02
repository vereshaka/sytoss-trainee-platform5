package com.sytoss.lessons.bdd.given;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.bdd.CucumberIntegrationTest;
import com.sytoss.lessons.bdd.common.TestExecutionContext;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TeacherDTO;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.java.en.Given;

import java.util.List;
import java.util.Optional;

public class TopicGiven extends CucumberIntegrationTest {

    @Given("topic exist")
    public void thisExamHasAnswers(List<TopicDTO> topics) {

        for (TopicDTO topic : topics) {
            Optional<TeacherDTO> optionalTeacherDTO = getTeacherConnector().findById(TestExecutionContext.getTestContext().getTeacherId());
            TeacherDTO teacherDTO = optionalTeacherDTO.orElse(null);

            DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(topic.getDiscipline().getName(), teacherDTO.getId());
            if (disciplineDTO == null) {
                disciplineDTO = topic.getDiscipline();
                disciplineDTO.setTeacher(teacherDTO);
                disciplineDTO = getDisciplineConnector().save(disciplineDTO);
            }
            TestExecutionContext.getTestContext().setDisciplineId(disciplineDTO.getId());
            TopicDTO topicResult = getTopicConnector().getByNameAndDisciplineId(topic.getName(), disciplineDTO.getId());
            topic.setDiscipline(disciplineDTO);
            if (topicResult == null) {
                topicResult = getTopicConnector().save(topic);
            }
            TestExecutionContext.getTestContext().setTopicId(topicResult.getId());
        }
    }

    @Given("^This discipline has \"(.*)\" project$")
    public void customerHasProject(String topicName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getReferenceById(TestExecutionContext.getTestContext().getDisciplineId());
        TopicDTO topicDTO = getTopicConnector().getByNameAndDisciplineId(topicName, TestExecutionContext.getTestContext().getDisciplineId());
        if (topicDTO == null) {
            topicDTO = new TopicDTO();
            topicDTO.setName(topicName);
            topicDTO.setDiscipline(disciplineDTO);
            topicDTO = getTopicConnector().save(topicDTO);
        }
        TestExecutionContext.getTestContext().setTopicId(topicDTO.getId());
    }

    @Given("^\"(.*)\" topic by \"(.*)\" discipline doesn't exist$")
    public void topicExist(String topicName, String disciplineName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByName(disciplineName);
        TopicDTO topic = getTopicConnector().getByNameAndDisciplineId(topicName, disciplineDTO.getId());
        if (topic != null) {
            getTopicConnector().delete(topic);
        }
    }

    @Given("topic with name {string} exist")
    public void topicWithExist(String name) {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Ivan");
        teacher.setLastName("Ivanov");
        TeacherDTO teacherDTO = new TeacherDTO();
        getTeacherConvertor().toDTO(teacher, teacherDTO);
        getTeacherConnector().save(teacherDTO);


        Discipline discipline = new Discipline();
        discipline.setId(1L);
        discipline.setName("Database");
        discipline.setTeacher(teacher);
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        getDisciplineConvertor().toDTO(discipline, disciplineDTO);
        getDisciplineConnector().save(disciplineDTO);

        Topic topic = new Topic();
        topic.setId(1L);
        topic.setName(name);
        topic.setDiscipline(discipline);
        TopicDTO topicDTO = new TopicDTO();
        getTopicConvertor().toDTO(topic, topicDTO);
        getTopicConnector().save(topicDTO);
        TestExecutionContext.getTestContext().setTopic(topic);
    }

    @Given("topic with name {string} and id {string} exist")
    public void topicWithNameAndIdExist(String name, String id) {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Ivan");
        teacher.setLastName("Ivanov");
        TeacherDTO teacherDTO = new TeacherDTO();
        getTeacherConvertor().toDTO(teacher, teacherDTO);
        getTeacherConnector().save(teacherDTO);


        Discipline discipline = new Discipline();
        discipline.setId(1L);
        discipline.setName("Database");
        discipline.setTeacher(teacher);
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        getDisciplineConvertor().toDTO(discipline, disciplineDTO);
        getDisciplineConnector().save(disciplineDTO);

        Topic topic = new Topic();
        topic.setId(Long.parseLong(id));
        topic.setName(name);
        topic.setDiscipline(discipline);
        TopicDTO topicDTO = new TopicDTO();
        getTopicConvertor().toDTO(topic, topicDTO);
        getTopicConnector().save(topicDTO);
        TestExecutionContext.getTestContext().setTopic(topic);
    }
}
