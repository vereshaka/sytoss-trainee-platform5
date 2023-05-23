package com.sytoss.lessons.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "GROUP")
public class GroupDTO {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_id_generator")
    @SequenceGenerator(name = "group_id_generator", sequenceName = "GROUP_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "DISCIPLINE_ID", referencedColumnName = "ID")
    private DisciplineDTO discipline;
}
