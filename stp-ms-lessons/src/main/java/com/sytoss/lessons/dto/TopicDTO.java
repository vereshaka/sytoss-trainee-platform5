package com.sytoss.lessons.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity(name = "TOPIC")
public class TopicDTO {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "topic_id_generator")
    @SequenceGenerator(name = "topic_id_generator", sequenceName = "TOPIC_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SHORT_DESCRIPTION")
    private String shortDescription;

    @Column(name = "FULL_DESCRIPTION")
    private String fullDescription;

    @Column(name = "DURATION")
    private Double duration;

    @Column(name = "ICON")
    private byte[] icon;

    @ManyToOne
    @JoinColumn(name = "DISCIPLINE_ID", referencedColumnName = "ID")
    private DisciplineDTO discipline;
}
