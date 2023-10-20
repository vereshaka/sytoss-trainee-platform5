package com.sytoss.lessons.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity(name = "DISCIPLINE")
@EntityListeners(AuditingEntityListener.class)
public class DisciplineDTO {

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

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Date createdDate;

    @LastModifiedBy
    private String lastModifiedBy;

    @LastModifiedDate
    private Date lastModifiedDate;

}
