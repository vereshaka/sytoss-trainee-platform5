package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.GroupDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupConnector extends JpaRepository<GroupDTO, Long> {

}
