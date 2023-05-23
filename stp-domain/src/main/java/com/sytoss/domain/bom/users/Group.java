package com.sytoss.domain.bom.users;

import com.sytoss.domain.bom.lessons.Discipline;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Group {

    private String id;

    private String name;

    private Discipline discipline;
}
