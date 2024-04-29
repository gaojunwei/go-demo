package com.gjw.innovation.controller;

import com.gjw.innovation.zk.ZkApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("zk")
public class ZookeeperController {
    @Resource
    private ZkApi zkApi;

    @GetMapping(value = "createNode")
    public boolean createNode(String path, String data) {
        log.info("zk 创建节点 {},{}", path, data);
        return zkApi.createNode(path, data);
    }
}