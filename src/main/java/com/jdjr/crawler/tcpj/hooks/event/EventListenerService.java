package com.jdjr.crawler.tcpj.hooks.event;

import com.jd.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 监听消息
 */
@Component
@Slf4j
public class EventListenerService {
    @EventListener
    public void handleEvent(MyEvent param) {
        logger.info("监听事件消息:{}", JSON.toJSONString(param));
    }

    @EventListener(value = MyEvent2.class)
    public void handleEvent2(MyEvent2 param) {
        logger.info("监听事件消息3:{}", JSON.toJSONString(param));
    }
}
