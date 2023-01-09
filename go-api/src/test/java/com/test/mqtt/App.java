package com.test.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.TimeUnit;

@Slf4j
public class App {
    public static void main(String[] args) throws InterruptedException {
        String subTopic = "gjw_test/#";
        String pubTopic = "gjw_test/1";
        String content = "Hello World";
        int qos = 2;
        String broker = "tcp://1.1.1.249:1883";
        String clientId = "client_id_001";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient client = new MqttClient(broker, clientId, persistence);

            // MQTT 连接选项
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName("emqx_tesssst");
            connOpts.setPassword("emqx_test_pafffssword".toCharArray());
            connOpts.setConnectionTimeout(10);
            connOpts.setKeepAliveInterval(10);
            // 保留会话
            connOpts.setCleanSession(true);

            // 设置回调
            client.setCallback(new OnMessageCallback());

            // 建立连接
            log.info("Connecting to broker: " + broker);
            client.connect(connOpts);

            log.info("Connected");
            log.info("Publishing message: " + content);

            // 订阅
            client.subscribe(subTopic);

            // 消息发布所需参数
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            client.publish(pubTopic, message);
            log.info("Message published");

            TimeUnit.SECONDS.sleep(5000);

            client.disconnect();
            log.info("Disconnected");
            client.close();
            System.exit(0);
        } catch (MqttException me) {
            log.info("reason " + me.getReasonCode());
            log.info("msg " + me.getMessage());
            log.info("loc " + me.getLocalizedMessage());
            log.info("cause " + me.getCause());
            log.info("excep " + me);
            me.printStackTrace();
        }
    }
}