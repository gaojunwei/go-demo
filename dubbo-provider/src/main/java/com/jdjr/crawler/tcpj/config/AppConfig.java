package com.jdjr.crawler.tcpj.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Setter
@Getter
public class AppConfig {
    @Value("${app.version}")
    private String appVersion;
}
