package com.gjw.innovation.service.impl;

import com.gjw.innovation.service.LocalLockService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class LocalLockServiceImpl implements LocalLockService {
    /**
     * 缓存配置
     */
    private static final Cache<String, ReentrantLock> lockCache = CacheBuilder.newBuilder()
            .expireAfterAccess(60, TimeUnit.SECONDS)//代表着最后一次访问了之后多久过期
            .maximumSize(1000)//指定缓存可以包含的最大条目数
            .weakValues()//缓存值采用弱引用
            .build();

    @Override
    public ReentrantLock getReentrantLock(String lockKey) throws ExecutionException {
        return lockCache.get(lockKey, ReentrantLock::new);
    }
}
