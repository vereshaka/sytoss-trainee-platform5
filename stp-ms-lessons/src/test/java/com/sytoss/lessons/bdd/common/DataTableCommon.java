package com.sytoss.lessons.bdd.common;

import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.GroupDTO;
import com.sytoss.lessons.dto.TeacherDTO;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.java.DataTableType;

import java.util.Map;

public class DataTableCommon {

    @DataTableType
    public TeacherDTO mapTeacher(Map<String, String> entry) {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setFirstName(entry.get("firstName"));
        teacherDTO.setLastName(entry.get("lastName"));

        return teacherDTO;
    }

    @DataTableType
    public DisciplineDTO mapDiscipline(Map<String, String> entry) {
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setName(entry.get("discipline"));

        if (entry.containsKey("teacherId")) {
            TeacherDTO teacherDTO = new TeacherDTO();
            teacherDTO.setId(Long.valueOf(entry.get("teacherId")));
            disciplineDTO.setTeacher(teacherDTO);
        }

        return disciplineDTO;
    }

    @DataTableType
    public TopicDTO mapTopic(Map<String, String> entry) {
        TopicDTO topic = new TopicDTO();
        DisciplineDTO discipline = new DisciplineDTO();
        topic.setName(entry.get("topic"));
        discipline.setName(entry.get("discipline"));
        topic.setDiscipline(discipline);
        return topic;
    }

    @DataTableType
    public GroupDTO mapGroups(Map<String, String> entry) {
        GroupDTO group = new GroupDTO();
        DisciplineDTO discipline = new DisciplineDTO();
        discipline.setName(entry.get("discipline"));
        group.setName(entry.get("group"));
        group.setDiscipline(discipline);
        return group;
    }
}
