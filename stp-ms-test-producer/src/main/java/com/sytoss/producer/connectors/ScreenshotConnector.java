package com.sytoss.producer.connectors;

import com.sytoss.producer.bom.ScreenshotModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScreenshotConnector extends MongoRepository<ScreenshotModel, String> {
}
