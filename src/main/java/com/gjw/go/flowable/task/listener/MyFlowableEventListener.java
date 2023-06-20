package com.gjw.go.flowable.task.listener;

import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.common.engine.api.delegate.event.FlowableEventType;

import java.util.Optional;

/**
 * 流程监听器，可全局监听整个流程
 */
@Slf4j
public class MyFlowableEventListener implements FlowableEventListener {
    @Override
    public void onEvent(FlowableEvent event) {
        FlowableEventType type = event.getType();
        log.info("Flowable事件监听 onEvent type:{} name:{}", type, Optional.ofNullable(event.getType()).map(item -> item.name()).orElse("name 为空"));
    }

    @Override
    public boolean isFailOnException() {
        log.info("isFailOnException ");
        return false;
    }

    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        return false;
    }

    @Override
    public String getOnTransaction() {
        return null;
    }
}
