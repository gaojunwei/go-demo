package com.test.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

@Slf4j
public class OnMessageCallback implements MqttCallbackExtended {
    public void connectionLost(Throwable cause) {
        // 连接丢失后，一般在这里面进行重连
        log.info("连接断开，可以做重连");
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // subscribe后得到的消息会执行到这里面
        log.info("接收消息主题:" + topic);
        log.info("接收消息Qos:" + message.getQos());
        log.info("接收消息内容:" + new String(message.getPayload()));
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        log.info("deliveryComplete---------" + token.isComplete());
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        log.info("connectComplete---------reconnect：{}，serverURI：{}", reconnect, serverURI);
    }
}