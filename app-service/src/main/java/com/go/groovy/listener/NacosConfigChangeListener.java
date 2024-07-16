package com.go.groovy.listener;

import com.alibaba.nacos.api.config.listener.Listener;

import java.util.concurrent.Executor;

public class NacosConfigChangeListener implements Listener {
    @Override
    public Executor getExecutor() {
        return null;
    }

    @Override
    public void receiveConfigInfo(String configInfo) {
        System.out.println("监听到脚本变化 = " + configInfo);
    }
}
