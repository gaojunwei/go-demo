package com.gjw.common.innovation.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@Setter
@Getter
public class MtgConfig {

    /**
     * 配置线程池
     */
    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(200, 200,
                120L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(),//无界，FIFO队列
                new ThreadFactoryBuilder().setNameFormat("exe-cmd-%d").build(),
                new ThreadPoolExecutor.AbortPolicy());
        //注册关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            threadPool.shutdown();
        }));
        return threadPool;
    }
}
