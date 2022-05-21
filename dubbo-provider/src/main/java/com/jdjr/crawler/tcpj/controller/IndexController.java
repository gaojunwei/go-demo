package com.jdjr.crawler.tcpj.controller;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("zk")
@Slf4j
public class IndexController {

    @Resource
    private Gson gson;

    
}
