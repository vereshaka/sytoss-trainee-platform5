package com.sytoss.domain.bom.lessons;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Topic {

    private Long id;

    private String name;

    private String shortDescription;

    private String fullDescription;

    private Double duration;

    private byte[] icon;

    private Discipline discipline;
}
