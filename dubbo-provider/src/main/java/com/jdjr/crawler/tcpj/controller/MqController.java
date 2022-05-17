package com.jdjr.crawler.tcpj.controller;

import com.google.gson.Gson;
import com.jdjr.crawler.tcpj.service.mq.rocket.RocketMqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("mq")
@Slf4j
public class MqController {
    /*@Resource
    private KafkaService kafkaService;*/
    @Resource
    private RocketMqService rocketMqService;
    @Resource
    private Gson gson;

    @GetMapping("send/rocket")
    public String sendRocket(@RequestParam String msg, @RequestParam(required = false) Integer c,@RequestParam String topic,@RequestParam Boolean order) {
        c = c == null ? 1 : c;
        for (int i = 0; i < c; i++) {
            Map<String,String> dataMap = new HashMap<>();
            dataMap.put("key","key-"+i);
            dataMap.put("data",msg+"-"+i);
            if(order == null || !order){
                rocketMqService.send(gson.toJson(dataMap),topic);
            }else {
                rocketMqService.sendOrderly(gson.toJson(dataMap),topic);
            }
        }
        return "success";
    }

    /*@GetMapping("send/kafka")
    public String sendKafka(@RequestParam String msg, @RequestParam(required = false) Integer c) {
        c = c == null ? 1 : c;
        for (int i = 0; i < c; i++) {
            kafkaService.send(msg);
        }
        return "success";
    }*/
}
