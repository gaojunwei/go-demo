package com.gjw.common.innovation.service;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

@Slf4j
public class MqttPushClient {
    private static String mqttHostUrl = "tcp://1.1.1.249:1883";
    private static String mqttUsername = "admin";
    private static String mqttPassword = "public";
    private static String mqttClientId = "client_id_123456";


    private static MqttClient mqttClient;

    public static MqttClient getClient() {
        return mqttClient;
    }

    public static void setClient(MqttClient client) {
        MqttPushClient.mqttClient = client;
    }

    /**
     * 编辑连接信息
     */
    private static MqttConnectOptions getOption() {
        //MQTT连接设置
        MqttConnectOptions option = new MqttConnectOptions();
        //设置是否清空session,false表示服务器会保留客户端的连接记录，true表示每次连接到服务器都以新的身份连接
        option.setCleanSession(false);
        //设置连接的用户名
        option.setUserName(mqttUsername);
        //设置连接的密码
        option.setPassword(mqttPassword.toCharArray());
        //设置超时时间 单位为秒
        option.setConnectionTimeout(5);
        //设置会话心跳时间 单位为秒
        option.setKeepAliveInterval(10);
        //setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
        //option.setWill(topic, "close".getBytes(StandardCharsets.UTF_8), 2, true);
        option.setMaxInflight(1000);
        return option;
    }

    /**
     * 发起连接
     */
    public static void connect() {
        MqttClient client;
        try {
            client = new MqttClient(mqttHostUrl, mqttClientId, new MemoryPersistence());
            MqttConnectOptions options = getOption();
            try {
                client.setCallback(new PushCallback());
                if (!client.isConnected()) {
                    client.connect(options);
                    logger.info("================>>>MQTT连接成功<<======================");

                } else {//这里的逻辑是如果连接不成功就重新连接
                    client.disconnect();
                    client.connect(options);
                    logger.info("===================>>>MQTT断连成功<<<======================");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            MqttPushClient.setClient(client);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 断线重连
     *
     * @throws Exception
     */
    public static Boolean reConnect() throws Exception {
        Boolean isConnected = false;
        if (null != mqttClient) {
            mqttClient.connect();
            if (mqttClient.isConnected()) {
                isConnected = true;
            }
        }
        return isConnected;
    }

    /**
     *
     * 订阅多个主题
     *
     * @param topic
     * @param qos
     */
    public void subscribe(String[] topic, int[] qos) {
        try {
            MqttPushClient.getClient().unsubscribe(topic);
            MqttPushClient.getClient().subscribe(topic, qos);
        } catch (MqttException e) {
            logger.error("errorMsg:{}",e.getMessage(),e);
        }
    }

    /**
     * 清空主题
     * @param topic
     */
    public void cleanTopic(String topic) {
        try {
            MqttPushClient.getClient().unsubscribe(topic);
        } catch (MqttException e) {
            logger.error("errorMsg:{}",e.getMessage(),e);
        }
    }
}
