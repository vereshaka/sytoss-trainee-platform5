package com.sytoss.lessons.services;

import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Teacher;

public abstract class AbstractService {

    protected AbstractUser getCurrentUser() {
        Teacher result = new Teacher() ;
        result.setId(1L);
        return result;
    }
}