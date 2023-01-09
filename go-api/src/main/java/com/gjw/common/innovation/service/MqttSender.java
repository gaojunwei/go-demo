package com.gjw.common.innovation.service;


import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@Slf4j
public class MqttSender {

    /**
     * 发布消息
     */
    public void publish(int qos, boolean retained, String topic, String pushMessage) {
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        try {
            message.setPayload(pushMessage.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        MqttTopic mTopic = MqttPushClient.getClient().getTopic(topic);
        if (null == mTopic) {
            logger.error("===================>>>MQTT topic 不存在<<=================");
        }
        MqttDeliveryToken token;
        try {
            token = mTopic.publish(message);
            token.waitForCompletion();
        } catch (MqttPersistenceException e) {
            logger.error("============>>>publish fail", e);
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
