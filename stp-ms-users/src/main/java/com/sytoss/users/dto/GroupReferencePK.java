package com.sytoss.users.dto;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class GroupReferencePK implements Serializable {

    private Long groupId;

    private Long student;
}

