package com.gjw.common.innovation.controller;

import com.gjw.common.innovation.service.MqttPushClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: gaojunwei
 * @Date: 2019/7/2 19:53
 * @Description:
 */
@RestController
@RequestMapping("/")
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @GetMapping("mqtt")
    public String index(@RequestParam("msg") String msg) throws MqttException {
        // 消息发布所需参数
        MqttMessage message = new MqttMessage(msg.getBytes());
        message.setQos(1);
        MqttPushClient.getClient().publish("gjw_test", message);
        return "success";
    }
}