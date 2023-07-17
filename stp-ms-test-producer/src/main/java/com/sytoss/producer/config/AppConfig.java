package com.sytoss.producer.config;

import com.sytoss.domain.bom.convertors.PumlConvertor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AppConfig {

    @Bean
    public PumlConvertor pumlConvertor() {
        return new PumlConvertor();
    }
}