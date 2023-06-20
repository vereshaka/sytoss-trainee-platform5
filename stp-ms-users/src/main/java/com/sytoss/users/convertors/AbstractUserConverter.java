package com.sytoss.users.convertors;

import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.users.dto.UserDTO;
import org.springframework.security.oauth2.jwt.Jwt;

public abstract class AbstractUserConverter {

    public void fromDTO(UserDTO source, AbstractUser destination) {
        destination.setId(source.getId());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
        destination.setEmail(source.getEmail());
        destination.setModerated(source.isModerated());
        destination.setPhoto(source.getPhoto());
    }

    public void fromDTO(Jwt source, AbstractUser destination) {
        destination.setEmail(source.getClaim("email"));
        destination.setFirstName(source.getClaim("given_name"));
        destination.setLastName(source.getClaim("family_name"));
    }

    public void toDTO(AbstractUser source, UserDTO destination) {
        destination.setId(source.getId());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
        destination.setEmail(source.getEmail());
        destination.setModerated(source.isModerated());
        destination.setPhoto(source.getPhoto());
    }
}
