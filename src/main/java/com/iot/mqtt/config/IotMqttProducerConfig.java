package com.iot.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageHandler;

/**
 * 生产者配置
 */
@Configuration
public class IotMqttProducerConfig {

    @Autowired
    private MqttConfig mqttConfig;

    @Bean
    public MqttPahoClientFactory mqttProducerClientFactory() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);
        options.setServerURIs(new String[]{mqttConfig.getServers()});
        options.setUserName("admin");
        options.setPassword("admin".toCharArray());

        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    @ServiceActivator(inputChannel = "iotMqttInputChannel_Producer")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(
                mqttConfig.getServerClientId() + "producer",
                mqttProducerClientFactory());
        messageHandler.setAsync(false);
        messageHandler.setDefaultQos(1);
        messageHandler.setDefaultTopic(mqttConfig.getDefaultTopic());

        return messageHandler;
    }
}
