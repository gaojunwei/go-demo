package com.gjw.common.innovation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = {"com.gjw.common.innovation.repository.**"})
public class TApplication {
    public static void main(String[] args) {
        SpringApplication.run(TApplication.class, args);
    }
}