package com.gjw.common.innovation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("info")
public class IndexController {
    @Resource
    private WebsocketController websocketController;

    @GetMapping
    public String propType() {
        return Integer.toString(websocketController.getCount());
    }
}
