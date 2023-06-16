package com.sytoss.users.connectors;

import com.sytoss.users.dto.TeacherDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherConnector extends JpaRepository<TeacherDTO, Long> {

    TeacherDTO getByFirstNameAndMiddleNameAndLastName(String firstname,String middlename, String lastname);

    TeacherDTO getByEmail(String email);
}
