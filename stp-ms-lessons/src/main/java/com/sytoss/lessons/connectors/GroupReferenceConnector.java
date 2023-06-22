package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.GroupReferenceDTO;
import com.sytoss.lessons.dto.GroupReferencePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupReferenceConnector extends JpaRepository<GroupReferenceDTO, GroupReferencePK> {

    List<GroupReferenceDTO> findByDisciplineId(Long disciplineId);

    GroupReferenceDTO findByGroupId(Long groupId);
}
