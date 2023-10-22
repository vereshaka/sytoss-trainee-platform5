package com.sytoss.lessons.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Entity(name = "DISCIPLINE")
public class DisciplineDTO extends Auditable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "discipline_id_generator")
    @SequenceGenerator(name = "discipline_id_generator", sequenceName = "DISCIPLINE_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TEACHER_ID")
    private Long teacherId;

    @Column(name = "SHORT_DESCRIPTION")
    private String shortDescription;

    @Column(name = "FULL_DESCRIPTION")
    private String fullDescription;

    @Column(name = "ICON")
    private byte[] icon;

    @Column(name = "CREATION_DATE")
    private Timestamp creationDate;

    @OneToMany
    @JoinTable(name = "group2discipline",
            joinColumns = @JoinColumn(name = "DISCIPLINE_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "GROUP_ID", referencedColumnName = "GROUP_ID"))
    private List<GroupReferenceDTO> groupReferences;

}
