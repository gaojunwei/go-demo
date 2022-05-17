package com.jdjr.crawler.tcpj.service.mq.rocket;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * RocketMq消费者
 */
@Slf4j
//@Component("gjwTopicListener")
//@RocketMQMessageListener(consumerGroup = "gjwGroup3", topic = "gjwTopics")
public class MyRocketMQListener implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        logger.info("消费消息 {}", message);
    }
}
