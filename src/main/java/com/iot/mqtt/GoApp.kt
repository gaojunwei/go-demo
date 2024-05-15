package com.iot.mqtt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.iot.mqtt.mapper")
public class GoApp {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GoApp.class);
        app.run(args);
    }
}

