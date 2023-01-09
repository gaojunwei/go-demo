package com.test.mqtt.sample;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

@Slf4j
public class SubscribeSample {
    public static void main(String[] args) {
        String broker = "tcp://1.1.1.249:1883";
        String topic = "mqtt/test";
        String username = "admin";
        String password = "public";
        String clientid = "subscribe_client_001";
        int qos = 2;

        try {
            MqttClient client = new MqttClient(broker, clientid, new MemoryPersistence());
            // 连接参数
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(10);
            //设置是否自动重连
            options.setAutomaticReconnect(true);
            // 设置回调
            client.setCallback(new MqttCallback() {
                //参考网址：https://www.ibm.com/docs/zh/ibm-mq/7.5?topic=ssfksj-7-5-0-com-ibm-mm-tc-doc-tc60390--htm
                /**
                 * 当通信错误导致连接断开时，就会调用 connectionLost。
                 */
                @Override
                public void connectionLost(Throwable cause) {
                    //做重连
                    log.info("connectionLost: " + cause.getMessage());
                    try {
                        client.connect();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                /**
                 * 当客户机的与预订主题相匹配的预订到达时，就会调用 messageArrived。topic 是发布主题，而不是预订过滤器。
                 * 如果过滤器中包含通配符，那么这两者可能不同。
                 * 如果此主题与客户机所创建的多个预订相匹配，那么客户机将接收到此发布的多个副本。如果客户机发布至它也预订了的某个主题，
                 * 那么它将接收到它自己的发布的副本。
                 * 如果使用值为 1 或 2 的 QoS 发送消息，那么该消息将由 MqttClientPersistence 类存储，
                 * 之后 MQTT 客户机将调用 messageArrived。messageArrived 的运行方式类似 deliveryComplete：它只能针对发布调用一次，
                 * 当 messageArrived 返回到 MQTT 客户机时，此发布的本地副本将由 MqttClientPersistence.remove 除去。
                 * 当 messageArrived 返回到 MQTT 客户机时，MQTT 客户机会中断其对主题和消息的引用。
                 * 如果应用程序客户机尚不具备对对象的引用，那么会对主题和消息对象进行垃圾回收。
                 */
                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    log.info("topic:{},Qos:{},content:{}", topic,message.getQos(),new String(message.getPayload()));
                }

                /**
                 * analysisContext
                 */
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    log.info("deliveryComplete---------" + token.isComplete());
                }

            });
            client.connect(options);
            client.subscribe(topic, qos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}