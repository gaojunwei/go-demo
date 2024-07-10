package com.go.starter.config;

import com.go.starter.service.CoderAcademyService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter({CoderAcademyPropertiesAutoConfig.class})
@ConditionalOnBean({CoderAcademyConfig.class})
public class CoderAcademyAutoConfig {

    /**
     * 利用@ConditionalOnMissingBean注解来确保仅在容器中尚无CoderAcademyService Bean时才进行创建操作。
     * 这样就避免了重复注册同一类型Bean导致的问题。
     */
    @Bean
    @ConditionalOnMissingBean
    public CoderAcademyService coderAcademyService(CoderAcademyConfig coderAcademyConfig) {
        return new CoderAcademyService(coderAcademyConfig);
    }
}