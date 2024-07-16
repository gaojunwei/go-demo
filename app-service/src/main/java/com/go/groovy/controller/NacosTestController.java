package com.go.groovy.controller;

import com.go.groovy.config.AppConfig;
import com.go.groovy.listener.MyConfigService;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nacos")
public class NacosTestController {
    @Resource
    private AppConfig appConfig;
    @Resource
    private MyConfigService myConfigService;

    @GetMapping
    public String test() {
        return "hello nacos " + appConfig.getValue();
    }

    @Scheduled(cron = "0/2 * * * * ?")
    public void test2() {
        System.out.println("配置项 gjw.test -> " + appConfig.getValue());
        System.out.println("脚本内容 -> "+ myConfigService.getConfigContent("groovy_script","dev_group"));
    }
}
