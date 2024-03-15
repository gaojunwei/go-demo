
package com.gjw.go;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GoApp {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GoApp.class);
        app.run(args);
    }
}
