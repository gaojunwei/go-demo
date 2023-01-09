package com.test.mqtt.sample;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.TimeUnit;

/**
 * 发布 MQTT 消息
 */
@Slf4j
public class PublishSample {
    private static String broker = "tcp://1.1.1.249:1883";
    private static String topic = "mqtt/test";
    private static String username = "admin";
    private static String password = "public";
    private static String clientId = "publish_client_001";
    private static int qos = 2;

    public static void main(String[] args) {
        try {
            MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence());
            // 连接参数
            MqttConnectOptions options = new MqttConnectOptions();
            // 设置用户名和密码
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            //设置是否清除会话
            options.setCleanSession(false);
            //设置连接超时时间
            options.setConnectionTimeout(10);
            //设置心跳间隔
            options.setKeepAliveInterval(10);
            //设置是否自动重连
            options.setAutomaticReconnect(true);
            // 连接
            client.connect(options);
            //发送消息
            sendMsg(100000000, client);
            // 关闭连接
            client.disconnect();
            // 关闭客户端
            client.close();
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void sendMsg(int count, MqttClient client) throws MqttException, InterruptedException {
        String content = "hello mqtt! ";
        int a = 0;
        while (a <= count) {
            // 创建消息并设置 QoS
            MqttMessage message = new MqttMessage((content+a).getBytes());
            message.setQos(qos);
            // 发布消息
            client.publish(topic, message);
            log.info("发布消息 topic:{},content:{}{}", topic, content, a);
            TimeUnit.MILLISECONDS.sleep(10);
            a++;
        }
    }
}