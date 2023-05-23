package com.sytoss.lessons.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "DISCIPLINE")
public class DisciplineDTO {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "discipline_id_generator")
    @SequenceGenerator(name = "discipline_id_generator", sequenceName = "DISCIPLINE_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NAME")
    private String name;
}