package com.sytoss.provider.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ImageDTO {

    @Id
    @GeneratedValue
    private Long id;

    private byte[] image;
}
