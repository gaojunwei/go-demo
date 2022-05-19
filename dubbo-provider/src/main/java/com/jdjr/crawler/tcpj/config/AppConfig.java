package com.jdjr.crawler.tcpj.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Setter
@Getter
@Slf4j
public class AppConfig {
    @Value("${app.version}")
    private String appVersion;

    @Bean
    public CuratorFramework curatorFramework(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client =
                CuratorFrameworkFactory.builder()
                        .connectString("127.0.0.1:2181")
                        .sessionTimeoutMs(5000)
                        .connectionTimeoutMs(5000)
                        .retryPolicy(retryPolicy)
                        .namespace("base")
                        .build();
        client.start();
        return client;
    }
}
