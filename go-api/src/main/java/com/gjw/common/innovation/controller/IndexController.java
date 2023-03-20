package com.gjw.common.innovation.controller;

import com.gjw.common.enums.SystemCodeEnums;
import com.gjw.common.innovation.controller.threadlocal.ThreadLocalTest;
import com.gjw.common.result.SingleResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: gaojunwei
 * @Date: 2019/7/2 19:53
 * @Description:
 */
@RestController
@RequestMapping("/")
@Slf4j
public class IndexController {

    @Value("${app.name}")
    private String appName;

    @RequestMapping("")
    public SingleResult<String> index() throws InterruptedException {
        SingleResult<String> result = new SingleResult<>();
        result.setCode(SystemCodeEnums.SUCCESS.getCode());
        result.setMsg(SystemCodeEnums.SUCCESS.getMsg());
        result.setData(appName);

        logger.info("AAAAAAAAAAA");
        System.out.println();
        ThreadLocalTest threadLocalTest = new ThreadLocalTest();
        threadLocalTest.test002();
        System.out.println();
        logger.info("BBBBBBB");
        return result;
    }
}