package com.sytoss.domain.bom.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student extends AbstractUser {

    private byte[] photo;

    private boolean isModerated;
}
