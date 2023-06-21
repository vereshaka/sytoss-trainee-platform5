package com.sytoss.lessons.dto;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class GroupReferencePK implements Serializable {

    private Long groupId;

    private Long disciplineId;
}
