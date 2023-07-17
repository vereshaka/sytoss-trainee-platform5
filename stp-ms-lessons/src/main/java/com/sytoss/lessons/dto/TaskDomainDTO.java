package com.sytoss.lessons.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "TASK_DOMAIN")
public class TaskDomainDTO {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_domain_id_generator")
    @SequenceGenerator(name = "task_domain_id_generator", sequenceName = "TASK_DOMAIN_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DATABASE_SCRIPT")
    private String databaseScript;

    @Column(name = "DATA_SCRIPT")
    private String dataScript;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "DISCIPLINE_ID", referencedColumnName = "ID")
    private DisciplineDTO discipline;

    @Column(name = "IMAGE")
    private byte[] image;

    @Column(name = "DESCRIPTION")
    private String description;
}