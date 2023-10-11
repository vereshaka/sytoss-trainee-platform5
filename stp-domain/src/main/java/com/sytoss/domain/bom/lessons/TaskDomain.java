package com.sytoss.domain.bom.lessons;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDomain {

    private Long id;

    private String name;

    private String databaseScript;

    private String dataScript;

    private Discipline discipline;

    private String shortDescription;

    private String fullDescription;

    private String dataImageName;

    private String dbImageName;
}
