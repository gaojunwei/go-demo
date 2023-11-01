package com.gjw.innovation.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;

public interface LocalLockService {
    /**
     * 获取对应的锁
     */
    ReentrantLock getReentrantLock(String lockKey) throws ExecutionException;
}
