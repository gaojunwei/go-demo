package com.gjw.innovation.controller;

import com.gjw.common.enums.CacheKey;
import com.gjw.common.result.SingleResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("info")
public class IndexController {

    @GetMapping
    public String getFun(HttpServletRequest request) {
        String authorization = request.getHeader(CacheKey.authorization.getValue());
        log.info("获取请求头 authorization:{}",authorization);
        return "get请求返回结果数据";
    }

    @PostMapping
    public SingleResult<Map<String,Object>> postFun(HttpServletRequest request) {
        String authorization = request.getHeader(CacheKey.authorization.getValue());
        log.info("获取请求头 authorization:{}",authorization);

        Map<String,Object> map = new HashMap<>();
        map.put("id",1L);
        map.put("name","小明");
        map.put("age",13);
        SingleResult<Map<String,Object>> result = new SingleResult<>();
        result.setCode("0");
        result.setMsg("success");
        result.setData(map);
        return result;
    }
}
