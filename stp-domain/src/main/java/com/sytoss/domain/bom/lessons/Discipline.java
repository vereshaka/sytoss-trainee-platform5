package com.sytoss.domain.bom.lessons;

import com.fasterxml.jackson.annotation.JsonView;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Teacher;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Discipline {

    @JsonView({Group.TeacherGroups.class})
    private Long id;

    @JsonView({Group.TeacherGroups.class})
    private String name;

    private Teacher teacher;

    private String shortDescription;

    private String fullDescription;

    private Double duration;

    private byte[] icon;

    private Timestamp creationDate;
}
