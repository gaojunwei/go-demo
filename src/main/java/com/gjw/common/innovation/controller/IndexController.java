package com.gjw.common.innovation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("info")
public class IndexController {

    @GetMapping
    public String getFun() {
        return "get请求返回结果数据";
    }

    @PostMapping
    public String postFun() {
        return "get请求返回结果数据";
    }
}
