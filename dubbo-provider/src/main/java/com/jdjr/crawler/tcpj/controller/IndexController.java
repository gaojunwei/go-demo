package com.jdjr.crawler.tcpj.controller;

import com.google.gson.Gson;
import com.jdjr.crawler.tcpj.utils.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("test")
@Slf4j
public class IndexController {

    @Resource
    private Gson gson;

    @GetMapping("c")
    public String test(){
        while (true){
            System.out.println(RandomUtils.nextInt(100));
        }
    }
}
