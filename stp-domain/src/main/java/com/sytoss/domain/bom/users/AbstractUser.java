package com.sytoss.domain.bom.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractUser {

    private Long id;

    private String firstName;

    private String lastName;

    private String middleName;

    private String email;

    private byte[] photo;

    private boolean isModerated;
}
