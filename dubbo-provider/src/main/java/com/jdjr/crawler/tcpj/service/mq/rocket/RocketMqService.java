package com.jdjr.crawler.tcpj.service.mq.rocket;


import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class RocketMqService {
    @Resource
    private RocketMqHelper rocketMqHelper;

    public void send(String msg,String topic){
        logger.info("RocketMQ producer,topic:{}, message:{}", topic, msg);
        rocketMqHelper.asyncSend(topic, MessageBuilder.withPayload(msg).build());
    }

    public void sendOrderly(String msg,String topic){
        rocketMqHelper.syncSendOrderly(topic, MessageBuilder.withPayload(msg).build(),"bb");
    }
}
