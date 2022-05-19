package com.jdjr.crawler.tcpj.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.zookeeper.CreateMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class ZkService {
    @Resource
    private CuratorFramework curatorFramework;

    public void createNode(String path,String data) throws Exception {
        curatorFramework.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(path,data.getBytes(StandardCharsets.UTF_8));
    }
}
