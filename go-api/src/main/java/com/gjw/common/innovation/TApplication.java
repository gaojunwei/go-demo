package com.gjw.common.innovation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gjw.common.innovation.mapper")
public class TApplication {
    public static void main(String[] args) {
        SpringApplication.run(TApplication.class, args);
    }
}