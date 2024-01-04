package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.DisciplineDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisciplineConnector extends JpaRepository<DisciplineDTO, Long>,
        PagingAndSortingRepository<DisciplineDTO, Long>,
        JpaSpecificationExecutor<DisciplineDTO> {

    DisciplineDTO getByNameAndTeacherId(String disciplineName, Long teacherId);

    List<DisciplineDTO> findByGroupReferencesGroupId(Long groupId);

    List<DisciplineDTO> findByTeacherIdOrderByCreationDateAsc(Long teacherId);
}
