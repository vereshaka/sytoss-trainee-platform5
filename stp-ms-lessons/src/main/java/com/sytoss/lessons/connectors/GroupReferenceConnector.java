package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.GroupReferenceDTO;
import com.sytoss.lessons.dto.GroupReferencePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupReferenceConnector extends JpaRepository<GroupReferenceDTO, GroupReferencePK> {

    List<GroupReferenceDTO> findByDisciplineId(Long discipline_id);
    void deleteAllByDisciplineId(Long discipline_id);

    GroupReferenceDTO findByGroupId(Long groupId);

    List<GroupReferenceDTO> findByDisciplineId_TeacherId(Long teacherId);
}
