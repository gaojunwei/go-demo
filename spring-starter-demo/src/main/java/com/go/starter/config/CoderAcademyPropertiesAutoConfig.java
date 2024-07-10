package com.go.starter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@EnableConfigurationProperties({CoderAcademyProperties.class})
@PropertySource(name = "defaultProperties", value = "classpath:/META-INF/my-default.properties")
@ConditionalOnClass
public class CoderAcademyPropertiesAutoConfig {
    @Bean
    public CoderAcademyConfig coderAcademyConfig(CoderAcademyProperties coderAcademyProperties) {
        CoderAcademyConfig coderAcademyConfig = new CoderAcademyConfig();
        coderAcademyConfig.setPort(coderAcademyProperties.getPort());
        coderAcademyConfig.setUrl(coderAcademyProperties.getUrl());
        coderAcademyConfig.setPassword(coderAcademyProperties.getPassword());
        coderAcademyConfig.setUserName(coderAcademyProperties.getUserName());
        return coderAcademyConfig;
    }
}