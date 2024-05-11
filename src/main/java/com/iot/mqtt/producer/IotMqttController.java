package com.iot.mqtt.producer;

import com.iot.mqtt.producer.param.SendMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * MQTT测试
 */
@Controller
@RequestMapping("/fun")
public class IotMqttController {

    @Autowired
    private IotMqttGateway mqttGateway;

    @RequestMapping("/testMqtt")
    @ResponseBody
    public String sendMqtt(@RequestBody SendMsg param) {
        mqttGateway.sendMessage2Mqtt(param.getTopic(), 1, param.getMessage());
        return "SUCCESS";
    }

}
