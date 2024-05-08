package com.gjw.innovation.controller;


import com.alibaba.fastjson2.JSON;
import com.gjw.innovation.common.enums.SystemCodeEnums;
import com.gjw.innovation.common.result.SingleResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 测试
 */
@RestController
@RequestMapping("/")
@Slf4j
public class IndexController {
    @Value("${app.attr1:'xxxx1'}")
    private String appAttr1;

    @Value("${app.attr2:'xxxx2'}")
    private String appAttr2;

    @RequestMapping("")
    public SingleResult<Map<String, Object>> index() throws UnknownHostException {
        SingleResult<Map<String, Object>> result = new SingleResult<>();
        result.setCode(SystemCodeEnums.SUCCESS.getCode());
        result.setMsg(SystemCodeEnums.SUCCESS.getMsg());

        Map<String, Object> data = new HashMap<>();
        data.put("appAttr1", appAttr1);
        data.put("appAttr2", appAttr2);
        data.put("appName", getHostname());

        result.setData(data);
        log.info("响应数据 result={}", JSON.toJSONString(result));
        return result;
    }

    @RequestMapping("/api/one")
    public SingleResult<Map<String, Object>> pathIndex() throws UnknownHostException {
        SingleResult<Map<String, Object>> result = new SingleResult<>();
        result.setCode(SystemCodeEnums.SUCCESS.getCode());
        result.setMsg(SystemCodeEnums.SUCCESS.getMsg());

        Map<String, Object> data = new HashMap<>();
        data.put("appAttr1", appAttr1);
        data.put("appAttr2", appAttr2);
        data.put("appName", getHostname());

        result.setData(data);
        log.info("响应数据 result={}", JSON.toJSONString(result));
        return result;
    }

    @RequestMapping("/go/one")
    public SingleResult<Map<String, Object>> goIndex() throws UnknownHostException {
        SingleResult<Map<String, Object>> result = new SingleResult<>();
        result.setCode(SystemCodeEnums.SUCCESS.getCode());
        result.setMsg(SystemCodeEnums.SUCCESS.getMsg());

        Map<String, Object> data = new HashMap<>();
        data.put("appAttr1", appAttr1);
        data.put("appAttr2", appAttr2);
        data.put("appName", getHostname());

        result.setData(data);
        log.info("响应数据 result={}", JSON.toJSONString(result));
        return result;
    }


    private String getHostname() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostName();
    }


    @Scheduled(fixedDelay = 1000)
    public void task() {
        log.info("打印日志 {}", UUID.randomUUID());
    }
}