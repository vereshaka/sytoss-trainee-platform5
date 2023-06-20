package com.sytoss.users.services;

import com.sytoss.users.services.exceptions.UserNotIdentifiedException;
import io.micrometer.common.util.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public abstract class AbstractService {

    protected Jwt getJwt() {
        return ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    protected String getMyEmail() {
        String email = getJwt().getClaim("email");
        if (StringUtils.isEmpty(email)) {
            throw new UserNotIdentifiedException("Could not identify user");
        }
        return email;
    }
}
