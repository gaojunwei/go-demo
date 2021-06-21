package com.jdjr.crawler.tcpj.hooks.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class MyApplicationEventPublisher {
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 发送事件通知
     */
    public void publish(String msg) {
        applicationEventPublisher.publishEvent(new MyEvent(msg));
        applicationEventPublisher.publishEvent(new MyEvent2(msg));
        logger.info("发送事件消息:{}", msg);
    }
}
