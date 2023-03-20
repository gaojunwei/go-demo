package com.gjw.common.innovation.common;

import org.slf4j.MDC;

import java.util.Map;

public class MdcRunnable implements Runnable{
    private Runnable runnable;
    private Map<String, String> mdcContext;
    private Map<String, Object> threadLocalContext;
    private Map<ThreadLocal<?>, ?> threadLocalMap;

    public MdcRunnable(Runnable runnable, Map<String, String> mdcContext, Map<String, Object> threadLocalContext, Map<ThreadLocal<?>, ?> threadLocalMap) {
        this.runnable = runnable;
        this.mdcContext = mdcContext;
        this.threadLocalContext = threadLocalContext;
        this.threadLocalMap = threadLocalMap;
    }

    @Override
    @SuppressWarnings("all")
    public void run() {
        Map<String, String> previous = MDC.getCopyOfContextMap();
        if (mdcContext == null) {
            MDC.clear();
        } else {
            MDC.setContextMap(mdcContext);
        }

        for (Map.Entry<ThreadLocal<?>, ?> entry : threadLocalMap.entrySet()) {
            ThreadLocal key = entry.getKey();
            key.set(entry.getValue());
        }
        try {
            runnable.run();
        } finally {
            if (previous == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(previous);
            }
            threadLocalMap.keySet().forEach(ThreadLocal::remove);
        }
    }
}
