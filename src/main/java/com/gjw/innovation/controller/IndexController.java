package com.gjw.innovation.controller;

import com.gjw.innovation.common.enums.SystemCodeEnums;
import com.gjw.innovation.common.result.BasicResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: gaojunwei
 * @Date: 2019/7/2 19:53
 * @Description:
 */
@RestController
@RequestMapping("/")
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping("")
    public BasicResult index(){
        BasicResult result = BasicResult.instance(SystemCodeEnums.SUCCESS.getCode(),SystemCodeEnums.SUCCESS.getMsg());
        return result;
    }
}