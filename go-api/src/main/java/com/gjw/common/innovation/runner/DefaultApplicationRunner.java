package com.gjw.common.innovation.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 优先级最低的实现
 *
 * 方法可以看出来参数都不一样，额外科普一下 Spring Boot 如何传递额外参数通过命令行
 * 执行 java -jar 传递给 main 方法，规则如下键值对 格式为 --K=V
 * 多个使用空格隔开值 多个空格隔开
 *
 * @author gaojunwei
 * @date 2019/11/4 11:01
 */
@Slf4j
@Component
public class DefaultApplicationRunner implements ApplicationRunner, Ordered {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        args.getOptionNames().forEach(System.out::println);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>");
        String[] sourceArgs = args.getSourceArgs();

        if (sourceArgs!=null){
            for (String sourceArg : sourceArgs) {
                System.out.println("sourceArg = " + sourceArg);
            }
        }

        List<String> foo = args.getOptionValues("foo");
        if (!CollectionUtils.isEmpty(foo)){
            foo.forEach(System.out::println);
        }

        List<String> nonOptionArgs = args.getNonOptionArgs();
        System.out.println("nonOptionArgs.size() = " + nonOptionArgs.size());
        nonOptionArgs.forEach(System.out::println);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE+1;
    }
}