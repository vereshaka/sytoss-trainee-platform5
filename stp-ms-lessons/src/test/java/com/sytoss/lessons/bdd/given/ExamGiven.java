package com.sytoss.lessons.bdd.given;

import com.sytoss.domain.bom.lessons.*;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.bdd.common.ExamAssigneeView;
import com.sytoss.lessons.bdd.common.ExamView;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupReferenceDTO;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TopicDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamAssigneeDTO;
import com.sytoss.lessons.dto.exam.assignees.ExamDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;

import java.sql.Timestamp;
import java.util.*;

public class ExamGiven extends LessonsIntegrationTest {

    @Given("^\"(.*)\" discipline has group with id (.*)$")
    public void disciplineHasGroup(String disciplineName, Long groupId) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, getTestExecutionContext().getDetails().getTeacherId());
        GroupReferenceDTO groupReferenceDTO = new GroupReferenceDTO(groupId, disciplineDTO, false);
        getGroupReferenceConnector().save(groupReferenceDTO);
        getTestExecutionContext().getDetails().setGroupReferenceId(groupId);
    }

    @Given("^this discipline with id (.*) has exams$")
    public void examExists(String disciplineId, DataTable exams) {
        List<ExamView> examList = exams.asMaps(String.class, String.class).stream().toList().stream().map(ExamView::new).toList();
        for (ExamView item : examList) {
            ExamDTO dto = new ExamDTO();
            dto.setName(item.getName());
            dto.setMaxGrade(Integer.valueOf(item.getMaxGrade()));
            dto.setTasks(new ArrayList<>());
            dto.setTeacherId(getTestExecutionContext().getDetails().getTeacherId());
            Long id = (Long) getTestExecutionContext().replaceId(disciplineId);
            dto.setDiscipline(getDisciplineConnector().getReferenceById(id));
            dto.setTopics(getTopicConnector().findByDisciplineIdOrderByName(id));
            List<String> taskIds = Arrays.stream(item.getTasks().split(",")).map(String::trim).toList();
            for (String taskId : taskIds) {
                id = (Long) getTestExecutionContext().replaceId(taskId);
                dto.getTasks().add(getTaskConnector().getReferenceById(id));
            }
            dto = getExamConnector().save(dto);
            getTestExecutionContext().registerId(item.getId(), dto.getId());
        }
    }

    @Given("^this discipline has assigned groups: (.*)")
    public void disciplineHasAssigneedGroups(String groupIds) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getReferenceById(getTestExecutionContext().getDetails().getDisciplineId());
        Arrays.stream(groupIds.split(",")).forEach(item -> {
            GroupReferenceDTO groupReferenceDTO = new GroupReferenceDTO(Long.valueOf(item.trim()), disciplineDTO, false);
            getGroupReferenceConnector().save(groupReferenceDTO);
        });
    }

    @Given("^this exams have assignees$")
    public void examAssigneesExists(DataTable assignees) {
        List<ExamAssigneeView> examAssigneeViews = assignees.asMaps(String.class, String.class).stream().toList().stream().map(ExamAssigneeView::new).toList();
        for (ExamAssigneeView item : examAssigneeViews) {
            ExamAssigneeDTO dto = new ExamAssigneeDTO();
            dto.setRelevantFrom(Timestamp.valueOf(item.getRelevantFrom()));
            dto.setRelevantTo(Timestamp.valueOf(item.getRelevantTo()));
            Long id = (Long) getTestExecutionContext().replaceId(item.getExamId());
            dto.setExam(getExamConnector().getReferenceById(id));
            dto = getExamAssigneeConnector().save(dto);
            getTestExecutionContext().registerId(item.getId(), dto.getId());
        }
    }

    @Given("^this discipline has exams$")
    public void examExists(DataTable exams) {
        List<ExamView> examList = exams.asMaps(String.class, String.class).stream().toList().stream().map(ExamView::new).toList();
        for (ExamView item : examList) {
            ExamDTO dto = getExamConnector().getByName(item.getName());
            if(dto==null){
                dto = new ExamDTO();
                dto.setName(item.getName());
            }
            dto.setMaxGrade(Integer.valueOf(item.getMaxGrade()));
            dto.setNumberOfTasks(Integer.valueOf(item.getTaskCount()));
            dto.setTasks(new ArrayList<>());
            dto.setDiscipline(getDisciplineConnector().getReferenceById(getTestExecutionContext().getDetails().getDisciplineId()));
            dto.setTeacherId(getTestExecutionContext().getDetails().getTeacherId());
            List<String> taskIds = Arrays.stream(item.getTasks().split(",")).map(String::trim).toList();
            List<TopicDTO> topicDTOS = new ArrayList<>();
            for (String taskId : taskIds) {
                Long id = (Long) getTestExecutionContext().replaceId(taskId);
                TaskDTO taskDTO = getTaskConnector().findById(id).orElse(null);
                if (taskDTO != null) {
                    dto.getTasks().add(taskDTO);
                    for (TopicDTO topicDTO : taskDTO.getTopics()) {
                        if (!topicDTOS.stream().map(TopicDTO::getId).toList().contains(topicDTO.getId())) {
                            topicDTOS.add(topicDTO);
                        }
                    }
                }

            }
            dto.setTopics(topicDTOS);
            if (!topicDTOS.isEmpty()) {
                dto.setDiscipline(topicDTOS.get(0).getDiscipline());
            }
            dto = getExamConnector().save(dto);
            getTestExecutionContext().registerId(item.getId(), dto.getId());

        }
    }

    @Given("exams with specific id exist")
    public void examsWithSpecificIdsExist(DataTable dataTable) {
        List<Map<String, String>> exams = dataTable.asMaps();
        for (Map<String, String> exam : exams) {
            String examKey = getTestExecutionContext().replaceId(exam.get("id")).toString();
            Long disciplineId = Long.parseLong(getTestExecutionContext().replaceId(exam.get("disciplineId")).toString());
            if (Objects.equals(examKey, getTestExecutionContext().replaceId(exam.get("id")).toString())) {
                ExamDTO examDTO = new ExamDTO();
                examDTO.setName(exam.get("name"));
                examDTO.setTeacherId(1L);
                DisciplineDTO disciplineDTO = getDisciplineConnector().findById(disciplineId).orElse(null);
                if (disciplineDTO != null) {
                    examDTO.setDiscipline(disciplineDTO);
                }
                examDTO = getExamConnector().save(examDTO);
                getTestExecutionContext().registerId(examKey, examDTO.getId());
            }
        }
    }

    @Given("^exam \"(.*)\" with (.*) tasks for \"(.*)\" discipline exists$")
    public void examExists(String examName, Integer numberOfTasks, String disciplineName, List<Topic> topics) {
        if(getExamConnector().getByName(examName)==null){
            Teacher teacher = new Teacher();
            teacher.setId(getTestExecutionContext().getDetails().getTeacherId());

            DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, getTestExecutionContext().getDetails().getTeacherId());
            Discipline discipline = new Discipline();
            discipline.setId(disciplineDTO.getId());
            discipline.setName(disciplineDTO.getName());
            discipline.setTeacher(teacher);

            Group group = new Group();
            group.setId(getTestExecutionContext().getDetails().getGroupReferenceId());


            for (Topic topic : topics) {
                if (topic.getDiscipline().getName().equals(disciplineName)) {
                    topic.setId(getTopicConnector().getByNameAndDisciplineId(topic.getName(), disciplineDTO.getId()).getId());
                    topic.setDiscipline(discipline);
                } else {
                    throw new RuntimeException("Wrong test data");
                }
                topic.getDiscipline().setTeacher(teacher);
            }

            List<Task> tasks = new ArrayList<>();
            for (int i = 0; i < numberOfTasks; i++) {
                Task task = new Task();

                task.setTaskDomain(new TaskDomain());
            }

            Exam exam = new Exam();
            exam.setName(examName);
            exam.setNumberOfTasks(numberOfTasks);
            exam.setTopics(topics);
            exam.setDiscipline(discipline);
            exam.setTeacher(teacher);
            exam.setTasks(tasks);

            ExamDTO examDTO = new ExamDTO();
            getExamConvertor().toDTO(exam, examDTO);
            getExamConnector().save(examDTO);
        }
    }
}
