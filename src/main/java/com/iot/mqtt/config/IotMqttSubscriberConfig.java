package com.iot.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

/**
 * 消息订阅配置
 */
@Configuration
public class IotMqttSubscriberConfig {

    @Autowired
    private MqttConfig mqttConfig;

    @Bean
    public MqttPahoClientFactory mqttClientSubscriberFactory() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setAutomaticReconnect(true);
        options.setServerURIs(new String[]{mqttConfig.getServers()});
        options.setUserName("admin");
        options.setPassword("admin".toCharArray());

        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MessageChannel iotMqttInputChannelSubscriber() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                mqttConfig.getClientId() + "subscriber",
                mqttClientSubscriberFactory(),
                "testtopic/gjw666","testtopic/gjw");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(iotMqttInputChannelSubscriber());
        return adapter;
    }

    /**
     * @author xiaofu
     * @description 消息订阅
     * @date 2020/6/8 18:20
     */
    @Bean
    @ServiceActivator(inputChannel = "iotMqttInputChannelSubscriber")
    public MessageHandler messageHandler() {

        return message -> {
            try {
                String string = message.getPayload().toString();
                System.out.println("接收到MQTT消息 getHeaders：" + message.getHeaders());
                System.out.println("接收到MQTT消息：" + string);
            } catch (MessagingException ex) {
                //logger.info(ex.getMessage());
            }
        };

    }
}


