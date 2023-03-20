package com.gjw.common.innovation.controller.threadlocal;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Map;
import java.util.Optional;

/**
 * Runnable任务保证，使之能够在子线程中传递父线程中的MDC信息
 */
@Slf4j
public class MdcRunnable implements Runnable{
    /**
     * 真正的任务
     */
    private Runnable runnable;
    /**
     * 存储Mdc内容
     */
    private Map<String,String> mdcContext;

    public MdcRunnable(Runnable runnable) {
        this.runnable = runnable;
        this.mdcContext = MDC.getCopyOfContextMap();
    }

    @Override
    public void run() {
        /**
         * 设置MDC线程变量
         */
        Optional.ofNullable(mdcContext).ifPresent(ct->MDC.setContextMap(ct));
        try {
            logger.info("开始任 父线程传播值：{}，线程变量值：{}",MyThreadLocal.inheritableThreadLocal.get(),MyThreadLocal.threadLocalCmdData.get());
            runnable.run();
            logger.info("任务结束");
        }finally {
            /**
             * 手动清除，预发内存溢出
             */
            MDC.clear();
            MyThreadLocal.inheritableThreadLocal.remove();
            MyThreadLocal.threadLocalCmdData.remove();
        }
    }
}
