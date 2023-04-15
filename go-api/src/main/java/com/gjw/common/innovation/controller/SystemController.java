package com.gjw.common.innovation.controller;

import com.alibaba.fastjson.JSON;
import com.gjw.common.enums.SystemCodeEnums;
import com.gjw.common.innovation.controller.threadlocal.ThreadLocalTest;
import com.gjw.common.result.SingleResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author: gaojunwei
 * @Date: 2019/7/2 19:53
 * @Description:
 */
@RestController
@RequestMapping("sys")
@Slf4j
public class SystemController {

    @Value("${app.name}")
    private String appName;

    @GetMapping("refresh")
    public SingleResult<String> index() throws InterruptedException {
        SingleResult<String> result = new SingleResult<>();
        result.setCode(SystemCodeEnums.SUCCESS.getCode());
        result.setMsg(SystemCodeEnums.SUCCESS.getMsg());
        result.setData(appName);
        return result;
    }

    @PostMapping("refresh")
    public SingleResult<String> post(@RequestBody Map<String,String> param,@RequestHeader("a_key") String headerA,
                                     @RequestHeader("b_key") String headerB) {
        logger.info("获取请求参数 param:{},a_key:{},b_key:{}", JSON.toJSONString(param),headerA,headerB);
        SingleResult<String> result = new SingleResult<>();
        result.setCode(SystemCodeEnums.SUCCESS.getCode());
        result.setMsg(SystemCodeEnums.SUCCESS.getMsg());
        return result;
    }
}