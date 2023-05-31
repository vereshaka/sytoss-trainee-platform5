package com.sytoss.domain.bom.lessons;

import com.sytoss.domain.bom.users.Teacher;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Discipline {

    private Long id;

    private String name;

    private Teacher teacher;
}
