package com.sytoss.provider.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ImageDTO {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "IMAGE_BYTES")
    private byte[] imageBytes;
}
