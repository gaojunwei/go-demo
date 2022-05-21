package com.jdjr.crawler.tcpj.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Setter
@Getter
@Slf4j
public class AppConfig {
    @Value("${app.version}")
    private String appVersion;
}
