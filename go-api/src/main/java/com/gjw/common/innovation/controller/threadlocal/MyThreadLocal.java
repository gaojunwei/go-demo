package com.gjw.common.innovation.controller.threadlocal;

/**
 * 全局ThreadLocal信息
 */
public class MyThreadLocal {
    /**
     * 线程变量
     */
    public static ThreadLocal<String> threadLocalCmdData = new ThreadLocal<>();
    /**
     * 父线程传播变量
     */
    public static InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();
}


