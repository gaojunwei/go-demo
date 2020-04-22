package com.gjw.common.innovation.runner;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 自定义启动运行逻辑及执行顺序
 * <p>
 * 方法可以看出来参数都不一样，额外科普一下 Spring Boot 如何传递额外参数通过命令行
 * 执行 java -jar 传递给 main 方法，规则如下键值对 格式为 --K=V
 * 多个使用空格隔开值 多个空格隔开
 *
 * @author gaojunwei
 * @date 2019/11/4 10:57
 */
@Slf4j
@Component
public class LowOrderCommandLineRunner implements CommandLineRunner, Ordered {

    @Override
    public void run(String... args) {
        System.out.println("LowOrderCommandLineRunner args:" + JSON.toJSONString(args));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}