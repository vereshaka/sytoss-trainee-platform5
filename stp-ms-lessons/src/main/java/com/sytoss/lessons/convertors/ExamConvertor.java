package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Exam;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.Topic;
import com.sytoss.domain.bom.lessons.examassignee.ExamAssignee;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TopicDTO;
import com.sytoss.lessons.dto.exam.assignees.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExamConvertor {

    private final TopicConvertor topicConvertor;
    private final TaskConvertor taskConvertor;
    private final ExamAssigneeConvertor examAssigneeConvertor;

    public void toDTO(Exam source, ExamDTO destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());

        destination.setNumberOfTasks(source.getNumberOfTasks());
        destination.setTeacherId(source.getTeacher().getId());
        destination.setMaxGrade(source.getMaxGrade());

        if (!source.getExamAssignees().isEmpty()) {
            for (ExamAssignee examAssignee : source.getExamAssignees()) {
                ExamAssigneeDTO examAssigneeDTO = new ExamAssigneeDTO();
                examAssigneeConvertor.toDTO(examAssignee, examAssigneeDTO);
                destination.getExamAssignees().add(examAssigneeDTO);
            }
        }
    }

    public void fromDTO(ExamDTO source, Exam destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
        if (source.getDiscipline() != null) {
            destination.setDiscipline(new Discipline());
            destination.getDiscipline().setId(source.getDiscipline().getId());
        }

        if (source.getTopics() != null) {
            List<Topic> topicList = new ArrayList<>();
            source.getTopics().forEach(topicDTO -> {
                Topic topic = new Topic();
                topicConvertor.fromDTO(topicDTO, topic);
                topicList.add(topic);
            });
            destination.setTopics(topicList);
        }

        if (source.getTasks() != null) {
            List<Task> tasksList = new ArrayList<>();

            if (!CollectionUtils.isEmpty(source.getTasks())) {
                source.getTasks().forEach(taskDTO -> {
                    Task task = new Task();
                    taskConvertor.fromDTO(taskDTO, task);
                    tasksList.add(task);
                });
            }
            destination.setTasks(tasksList);
        }
        destination.setNumberOfTasks(source.getNumberOfTasks());
        Teacher teacher = new Teacher();
        teacher.setId(source.getTeacherId());
        destination.setTeacher(teacher);
        destination.setMaxGrade(source.getMaxGrade());

        for (ExamAssigneeDTO examAssigneeDTO : source.getExamAssignees()) {
            ExamAssignee examAssignee = new ExamAssignee();
            examAssigneeConvertor.fromDTO(examAssigneeDTO, examAssignee);
            destination.getExamAssignees().add(examAssignee);
        }
    }
}
