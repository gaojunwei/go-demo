package com.gjw.demo.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.stereotype.Service;

@Service
public class DemoService {

    @SentinelResource(value = "sayHello", blockHandler = "handleBlock")
    public String sayHello() {
        return "Hello from provider";
    }

    // 限流或降级时调用的方法
    public String handleBlock(BlockException ex) {
        return "请求被限流或系统繁忙，请稍后再试。";
    }
}
