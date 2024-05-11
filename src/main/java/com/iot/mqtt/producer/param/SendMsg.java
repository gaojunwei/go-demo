package com.iot.mqtt.producer.param;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SendMsg {
    private String topic;
    private String message;
}
