package com.sytoss.common;

import com.sytoss.domain.bom.exceptions.business.UserNotIdentifiedException;
import com.sytoss.domain.bom.users.AbstractUser;
import io.micrometer.common.util.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public abstract class AbstractStpService {

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

    protected String getMyId() {
        String id = getJwt().getClaim("id");
        if (StringUtils.isEmpty(id)) {
            throw new UserNotIdentifiedException("Could not identify user");
        }
        return id;
    }

    protected String getMyUid() {
        String id = getJwt().getClaim("sub");
        if (StringUtils.isEmpty(id)) {
            throw new UserNotIdentifiedException("Could not identify user");
        }
        return id;
    }

    protected AbstractUser getCurrentUser() {
        return getJwt().getClaim("user");
    }
}
