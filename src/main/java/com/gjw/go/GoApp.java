package com.gjw.go;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ext.gaojunwei1
 * @date 2023/5/29
 */
@SpringBootApplication
public class GoApp {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GoApp.class);
        app.run(args);
    }
}
