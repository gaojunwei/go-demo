package com.gjw.common.innovation;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TApplication {
    public static void main(String[] args) {
        SpringApplication.run(TApplication.class, args);
    }
}