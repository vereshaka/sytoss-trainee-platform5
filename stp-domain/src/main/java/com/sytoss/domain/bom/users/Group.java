package com.sytoss.domain.bom.users;

import com.fasterxml.jackson.annotation.JsonView;
import com.sytoss.domain.bom.lessons.Discipline;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Group {

    @JsonView({Group.TeacherGroups.class})
    private Long id;

    private String name;

    @JsonView({Group.TeacherGroups.class})
    private List<Discipline> disciplines;

    private int countOfStudents;

    public static class TeacherGroups {}
}
