package com.go.starter.service;

import com.go.starter.config.CoderAcademyConfig;

public class CoderAcademyService {

    private final CoderAcademyConfig coderAcademyConfig;

    public String sayHello() {
        return "Hello " + coderAcademyConfig.getUrl() + " -> " + coderAcademyConfig.getPort() + " -> " + coderAcademyConfig.getUserName() + " -> " + coderAcademyConfig.getPassword();
    }

    public CoderAcademyService(CoderAcademyConfig coderAcademyConfig) {
        this.coderAcademyConfig = coderAcademyConfig;
    }
}