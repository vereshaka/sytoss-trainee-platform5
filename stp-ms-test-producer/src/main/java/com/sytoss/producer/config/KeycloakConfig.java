package com.sytoss.producer.config;

import com.sytoss.common.config.CommonKeycloakConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class KeycloakConfig extends CommonKeycloakConfig {

}
