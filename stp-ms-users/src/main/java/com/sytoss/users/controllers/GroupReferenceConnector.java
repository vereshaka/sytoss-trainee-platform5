package com.sytoss.users.controllers;

import com.sytoss.users.dto.GroupReferenceDTO;
import com.sytoss.users.dto.GroupReferencePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupReferenceConnector extends JpaRepository<GroupReferenceDTO, GroupReferencePK> {
}

