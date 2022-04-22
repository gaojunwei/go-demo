package com.jdjr.crawler.tcpj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 程序入口
 */
@SpringBootApplication(scanBasePackages = "com.jdjr.crawler.tcpj")
@ImportResource(locations = {"classpath:spring-*.xml"})
@EnableScheduling
public class ProviderApp {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApp.class, args);
    }
}
