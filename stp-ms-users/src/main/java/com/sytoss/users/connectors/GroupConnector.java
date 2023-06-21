package com.sytoss.users.connectors;

import com.sytoss.users.dto.GroupDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupConnector extends JpaRepository<GroupDTO, Long> {

    GroupDTO getByName(String groupName);

}
