package com.sytoss.provider.connector;

import com.sytoss.provider.dto.ImageDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageConnector extends JpaRepository<ImageDTO, Long> {

}
