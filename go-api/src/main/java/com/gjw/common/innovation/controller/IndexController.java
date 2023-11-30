package com.gjw.common.innovation.controller;

import com.alibaba.fastjson.JSON;
import com.gjw.common.enums.SystemCodeEnums;
import com.gjw.common.result.SingleResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: gaojunwei
 * @Date: 2019/7/2 19:53
 */
@RestController
@RequestMapping("/")
@Slf4j
public class IndexController {
    @Value("${app.attr1}")
    private String appAttr1;

    @Value("${app.attr2}")
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
        logger.info("响应数据 result={}", JSON.toJSONString(result));
        return result;
    }

    private String getHostname() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostName();
    }
}