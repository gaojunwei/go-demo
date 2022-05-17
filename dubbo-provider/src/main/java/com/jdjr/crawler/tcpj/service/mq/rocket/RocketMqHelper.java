package com.jdjr.crawler.tcpj.service.mq.rocket;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * RocketMq助手
 */
@Slf4j
@Component
public class RocketMqHelper {

    /**
     * rocketmq模板注入
     */
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private Gson gson;

    @PostConstruct
    public void init() {
        logger.info("---RocketMq助手初始化---");
    }

    /**
     * 发送异步消息
     *
     * @param topic   消息Topic
     * @param message 消息实体
     */
    public void asyncSend(String topic, Message<?> message) {
        rocketMQTemplate.asyncSend(topic, message, getDefaultSendCallBack());
    }

    /**
     * 异步发送消息，指定回调函数
     *
     * @param topic        消息Topic
     * @param message      消息实体
     * @param sendCallback 回调函数
     */
    public void asyncSend(String topic, Message<?> message, SendCallback sendCallback) {
        rocketMQTemplate.asyncSend(topic, message, sendCallback);
    }

    /**
     * 异步发送消息，指定超时时间
     *
     * @param topic        消息Topic
     * @param message      消息实体
     * @param sendCallback 回调函数
     * @param timeout      超时时间
     */
    public void asyncSend(String topic, Message<?> message, SendCallback sendCallback, long timeout) {
        rocketMQTemplate.asyncSend(topic, message, sendCallback, timeout);
    }

    /**
     * 异步发送延时队列消息
     *
     * @param topic        消息Topic
     * @param message      消息实体
     * @param sendCallback 回调函数
     * @param timeout      超时时间
     * @param delayLevel   延迟消息的级别
     */
    public void asyncSend(String topic, Message<?> message, SendCallback sendCallback, long timeout, int delayLevel) {
        rocketMQTemplate.asyncSend(topic, message, sendCallback, timeout, delayLevel);
    }

    /**
     * 同步发送顺序消息
     */
    public void syncSendOrderly(String topic, Message<?> message, String hashKey) {
        logger.info("同步发送顺序消息，topic:{},hashKey:{},msg:{}",topic,hashKey,gson.toJson(message));
        rocketMQTemplate.syncSendOrderly(topic, message, hashKey);
    }

    /**
     * 异步发送顺序消息
     */
    public void syncSendOrderly(String topic, Message<?> message, String hashKey, long timeout) {
        logger.info("异步发送顺序消息，topic:" + topic + ",hashKey:" + hashKey + ",timeout:" + timeout);
        rocketMQTemplate.asyncSendOrderly(topic, message, hashKey, getDefaultSendCallBack(), timeout);
    }

    /**
     * 默认CallBack函数
     */
    private SendCallback getDefaultSendCallBack() {
        return new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                logger.info("---发送MQ成功---");
            }

            @Override
            public void onException(Throwable throwable) {
                logger.error("---发送MQ失败---" + throwable.getMessage(), throwable.getMessage());
            }
        };
    }

    @PreDestroy
    public void destroy() {
        logger.info("---RocketMq助手注销---");
    }
}
