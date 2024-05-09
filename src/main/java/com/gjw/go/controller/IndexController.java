package com.gjw.go.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/log")
@Validated
public class IndexController {
    @Resource
    private LoggingSystem loggingSystem;


    @GetMapping("/change")
    public String changeLogLevel(@RequestParam(required = false) String loggerName, @RequestParam LogLevel level){
        loggingSystem.setLogLevel(loggerName,level);
        return "success";
    }
}
