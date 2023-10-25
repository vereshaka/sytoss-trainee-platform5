package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.DisciplineDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisciplineConnector extends JpaRepository<DisciplineDTO, Long>, PagingAndSortingRepository<DisciplineDTO, Long> {

    DisciplineDTO getByNameAndTeacherId(String disciplineName, Long teacherId);

    Page<DisciplineDTO> findByTeacherIdOrderByCreationDateDesc(Long teacherId, Pageable pageable);

    List<DisciplineDTO> findByGroupReferencesGroupId(Long groupId);
}
