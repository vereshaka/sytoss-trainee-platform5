package com.sytoss.users.connectors;

import com.sytoss.users.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserConnector extends JpaRepository<UserDTO, Long> {

    UserDTO getByFirstNameAndLastName(String firstname, String lastname);

    UserDTO getByEmail(String email);

    UserDTO getByEncryptedId(String id);
}
