package com.sytoss.domain.bom.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student extends AbstractUser {

    private String photo;

    private boolean isModerated;
}
