package com.sytoss.producer.services;

import com.sytoss.domain.bom.users.AbstractUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public abstract class AbstractService {

    protected AbstractUser getCurrentUser() {
       return ((Jwt)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaim("user");
    }
}
