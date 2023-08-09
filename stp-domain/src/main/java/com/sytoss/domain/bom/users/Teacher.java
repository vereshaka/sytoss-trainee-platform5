package com.sytoss.domain.bom.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Teacher extends AbstractUser {
    @Override
    public String getType() {
        return "teacher";
    }
}
