package com.gjw.common.innovation.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 自定义启动运行逻辑
 * <p>
 * 方法可以看出来参数都不一样，额外科普一下Spring Boot如何传递额外参数通过命令行
 * 执行java -jar传递给main方法，规则如下键值对 格式为--K=V
 * 多个使用空格隔开值 多个空格隔开
 */
@Slf4j
@Component
public class HighOrderCommandLineRunner implements CommandLineRunner, Ordered {

    @Override
    public void run(String... args) {
        log.info("项目启动");
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}