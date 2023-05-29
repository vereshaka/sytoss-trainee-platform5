package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.TeacherDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherConnector extends JpaRepository<TeacherDTO, Long> {

    TeacherDTO getByLastName(String lastName);

    TeacherDTO getByLastNameAndFirstName(String lastName, String firstName);

}
