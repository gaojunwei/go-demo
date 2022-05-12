package com.jdjr.crawler.tcpj.controller;

import com.jdjr.crawler.tcpj.service.mq.kafka.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("mq")
@Slf4j
public class MqController {
    @Resource
    private KafkaService kafkaService;

    @GetMapping("send/kafka")
    public String sendKafka(@RequestParam String msg, @RequestParam(required = false) Integer c) {
        c = c == null ? 1 : c;
        for (int i = 0; i < c; i++) {
            kafkaService.send(msg);
        }
        return "success";
    }
}
