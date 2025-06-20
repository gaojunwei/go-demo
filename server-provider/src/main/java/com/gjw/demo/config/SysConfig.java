package com.gjw.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@RefreshScope
@Configuration
@Setter
@Getter
public class SysConfig {
    @Value("${test.name.xxx:xxxx}")
    private String userName;
}
