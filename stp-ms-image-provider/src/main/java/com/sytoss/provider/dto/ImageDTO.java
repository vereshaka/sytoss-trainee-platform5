package com.sytoss.provider.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity(name = "IMAGE")
public class ImageDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_id_generator")
    @SequenceGenerator(name = "image_id_generator", sequenceName = "IMAGE_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "IMAGE_BYTES")
    private byte[] imageBytes;
}
