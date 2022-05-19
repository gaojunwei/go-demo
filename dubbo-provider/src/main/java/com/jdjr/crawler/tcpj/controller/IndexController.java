package com.jdjr.crawler.tcpj.controller;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("zk")
@Slf4j
public class IndexController {

    @Resource
    private CuratorFramework curatorFramework;
    @Resource
    private Gson gson;

    /**
     * 创建临时节点
     */
    @GetMapping("ls/create")
    public String lsCreate(@RequestParam String path, @RequestParam String data, @RequestParam Boolean sort) throws Exception {
        String str;
        if (sort != null && sort) {
            str = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path, data.getBytes(StandardCharsets.UTF_8));
        } else {
            str = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, data.getBytes(StandardCharsets.UTF_8));
        }
        return "success " + str;
    }

    /**
     * 创建永久节点
     */
    @GetMapping("yj/create")
    public String yjCreate(@RequestParam String path, @RequestParam String data, @RequestParam Boolean sort) throws Exception {
        String str;
        if (sort != null && sort) {
            str = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(path, data.getBytes(StandardCharsets.UTF_8));
        } else {
            str = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, data.getBytes(StandardCharsets.UTF_8));
        }
        return "success " + str;
    }

    /**
     * 删除节点
     */
    @GetMapping("del")
    public String del(@RequestParam String path, @RequestParam Boolean delChild) throws Exception {
        if (delChild != null && delChild) {
            curatorFramework.delete().deletingChildrenIfNeeded().forPath(path);
        } else {
            curatorFramework.delete().forPath(path);
        }
        return "success";
    }

    /**
     * 修改节点数据
     */
    @GetMapping("update")
    public String update(@RequestParam String path, @RequestParam String val, @RequestParam Integer version) throws Exception {
        curatorFramework.setData().withVersion(StringUtils.isEmpty(version) ? 0 : version).forPath(path, val.getBytes(StandardCharsets.UTF_8));
        return "success";
    }

    /**
     * 获取节点状态
     */
    @GetMapping("getStat")
    public String getStat(@RequestParam String path) throws Exception {
        Stat stat = curatorFramework.checkExists().forPath(path);
        return "success " + gson.toJson(stat);
    }

    /**
     * 获取节点状态
     */
    @GetMapping("getData")
    public String getData(@RequestParam String path) throws Exception {
        String str = new String(curatorFramework.getData().forPath(path), "utf-8");
        return "success " + str;
    }

    /**
     * 获取节点状态
     */
    @GetMapping("exist")
    public String exist(@RequestParam String path) throws Exception {
        Stat stat = curatorFramework.checkExists().forPath(path);
        return "success " + gson.toJson(stat);
    }

    /**
     * 监听节点变化
     */
    @GetMapping("watch")
    public String watch(@RequestParam String path) throws Exception {
        //CuratorCache curatorCache = CuratorCache.build(curatorFramework, path, CuratorCache.Options.SINGLE_NODE_CACHE);//只监听本节点的通知
        CuratorCache curatorCache = CuratorCache.build(curatorFramework, path);//监听所有子节点通知
        curatorCache.listenable().addListener((type, oldData, data) -> logger.info("监听节点发送变化 \n type:{},\n oldData:{},\n data:{}", gson.toJson(type), gson.toJson(oldData), gson.toJson(data)));
        curatorCache.start();
        return "success";
    }


    public class Task implements Runnable{



        @Override
        public void run() {

        }
    }

}
