package com.sytoss.domain.bom.users;

import com.fasterxml.jackson.annotation.JsonView;
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
