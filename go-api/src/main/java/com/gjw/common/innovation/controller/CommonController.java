package com.gjw.common.innovation.controller;

import com.alibaba.fastjson.JSON;
import com.gjw.common.enums.SystemCodeEnums;
import com.gjw.common.result.BasicResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public")
@Slf4j
public class CommonController {
    @RequestMapping("mdc")
    public BasicResult one() {
        logger.info("MDC 链路方法测试Demo");
        BasicResult result = SystemCodeEnums.SUCCESS.applyValue();
        logger.info(JSON.toJSONString(result));
        return result;
    }
}
