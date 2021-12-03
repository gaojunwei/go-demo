package com.jdjr.crawler.tcpj.controller;

import com.jdjr.crawler.tcpj.common.enums.SystemCodeEnums;
import com.jdjr.crawler.tcpj.common.result.BasicResult;
import com.jdjr.crawler.tcpj.hooks.event.MyApplicationEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 测试
 */
@RestController
@RequestMapping("/")
@Slf4j
public class TestController {
    @Resource
    private MyApplicationEventPublisher myApplicationEventPublisher;

    /**
     * 发送通知
     */
    @GetMapping("test")
    public BasicResult publish(String msg) {
        System.out.println("清除");
        return SystemCodeEnums.SUCCESS.applyValue();
    }
}
