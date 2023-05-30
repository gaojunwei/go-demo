package com.gjw.go.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 测试配置类
 */
@Configuration
@Profile({"local","junit"})
public class TestConfiguration {

    @Bean
    public H2Flusher h2Flusher(JdbcTemplate jdbcTemplate) {
        return new H2Flusher(jdbcTemplate, "DB/init_table.sql");
    }
}

