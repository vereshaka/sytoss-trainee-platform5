package com.sytoss.domain.bom.users;

import com.fasterxml.jackson.annotation.JsonView;
import com.sytoss.domain.bom.lessons.Discipline;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Group {

    @JsonView({Group.TeacherGroups.class})
    private Long id;

    private String name;

    @JsonView({Group.TeacherGroups.class})
    private Discipline discipline;

    private int countOfStudents;

    public static class TeacherGroups {}
}
