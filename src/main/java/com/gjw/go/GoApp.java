
package com.gjw.go;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GoApp {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GoApp.class);
        app.run(args);
    }
}
