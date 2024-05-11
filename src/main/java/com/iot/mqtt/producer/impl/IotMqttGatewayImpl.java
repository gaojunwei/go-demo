package com.iot.mqtt.producer.impl;

import com.iot.mqtt.producer.IotMqttGateway;
import jakarta.annotation.Resource;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 发送MQTT消息
 */
@Component
public class IotMqttGatewayImpl implements IotMqttGateway {
    @Resource(name = "iotMqttInputChannel_Producer")
    private MessageChannel messageChannel;


    @Override
    public void sendMessage2Mqtt(String data) {
        messageChannel.send(new Message<String>() {
            @Override
            public String getPayload() {
                return data;
            }

            @Override
            public MessageHeaders getHeaders() {
                Map<String, Object> map = new HashMap<>();
                map.put(MqttHeaders.TOPIC, "mqtt_test_topic");
                return new MessageHeaders(map);
            }
        });
    }

    @Override
    public void sendMessage2Mqtt(String data, String topic) {
        messageChannel.send(new Message<String>() {
            @Override
            public String getPayload() {
                return data;
            }

            @Override
            public MessageHeaders getHeaders() {
                Map<String, Object> map = new HashMap<>();
                map.put(MqttHeaders.TOPIC, topic);
                return new MessageHeaders(map);
            }
        });

    }

    @Override
    public void sendMessage2Mqtt(String topic, int qos, String payload) {
        messageChannel.send(new Message<String>() {
            @Override
            public String getPayload() {
                return payload;
            }

            @Override
            public MessageHeaders getHeaders() {
                Map<String, Object> map = new HashMap<>();
                map.put(MqttHeaders.TOPIC, topic);
                map.put(MqttHeaders.QOS, qos);
                return new MessageHeaders(map);
            }
        });
    }
}
