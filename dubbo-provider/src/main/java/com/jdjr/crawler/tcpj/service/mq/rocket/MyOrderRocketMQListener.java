package com.jdjr.crawler.tcpj.service.mq.rocket;

import com.jdjr.crawler.tcpj.utils.ThreadSleepUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * RocketMq消费者
 */
@Slf4j
@Component("gjwtestOrderListener")
@RocketMQMessageListener(consumerGroup = "gjwGroup1",topic = "gjwtestOrder",consumeMode = ConsumeMode.ORDERLY)
public class MyOrderRocketMQListener implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        logger.info("顺序消费消息 {}",message);
        ThreadSleepUtils.sleep(1);
    }
}
