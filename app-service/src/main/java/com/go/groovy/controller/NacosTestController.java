package com.go.groovy.controller;

import com.go.groovy.config.AppConfig;
import com.go.groovy.groovy.service.cache.GroovyCache;
import com.go.groovy.groovy.service.enums.GroovyScriptEnum;
import com.go.groovy.listener.MyConfigService;
import jakarta.annotation.Resource;
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
    @Resource
    private GroovyCache groovyCache;

    @GetMapping
    public String test() {
        return "hello nacos " + appConfig.getValue();
    }

    //@Scheduled(cron = "0/2 * * * * ?")
    public void test2() {
        System.out.println("配置项 gjw.test -> " + appConfig.getValue());
        System.out.println("脚本内容 -> " + myConfigService.getConfigContent("groovy_script", "dev_group"));
    }

    @GetMapping("/groovy1")
    public String groovy1() {
        String result = groovyCache.run(GroovyScriptEnum.SPRING_METHOD_1,null);
        return result.toString();
    }

    @GetMapping("/groovy2")
    public String groovy2() {
        String result = groovyCache.run(GroovyScriptEnum.SPRING_METHOD_2,new Object[]{1,2});
        return result.toString();
    }
}
