package com.gjw.go.flowable.task.listener;

import liquibase.pro.packaged.S;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.event.impl.FlowableProcessEventImpl;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 流程监听器，可全局监听整个流程
 */
@Slf4j
public class MyFlowableEventListener implements FlowableEventListener {
    private static Map<String,String> statusMap = new HashMap<>();
    static {
        statusMap.put("unPass","fail");
        statusMap.put("pass","success");
    }

    @Override
    public void onEvent(FlowableEvent event) {
        FlowableProcessEventImpl processEvent = event instanceof FlowableProcessEventImpl?(FlowableProcessEventImpl) event:null;
        log.info("流程监听器  type:{},getProcessInstanceId:{}", event.getType(), Optional.ofNullable(processEvent).map(item->item.getProcessInstanceId()).orElse(""));
        if (FlowableEngineEventType.PROCESS_CREATED == event.getType()) {
            log.info("枚举值  PROCESS_CREATED");
        }
        if (FlowableEngineEventType.PROCESS_COMPLETED_WITH_ERROR_END_EVENT == event.getType()) {
            log.info("枚举值  PROCESS_COMPLETED_WITH_ERROR_END_EVENT");
        }
        if (FlowableEngineEventType.PROCESS_COMPLETED_WITH_ESCALATION_END_EVENT == event.getType()) {
            log.info("枚举值  PROCESS_COMPLETED_WITH_ESCALATION_END_EVENT");
        }
        if (FlowableEngineEventType.PROCESS_COMPLETED_WITH_TERMINATE_END_EVENT == event.getType()) {
            log.info("枚举值  PROCESS_COMPLETED_WITH_TERMINATE_END_EVENT");
        }
        if (FlowableEngineEventType.PROCESS_COMPLETED == event.getType()) {
            log.info("枚举值  PROCESS_COMPLETED");
            String status = Optional.ofNullable(processEvent).map(item->(String)item.getExecution().getVariable("businessStatus")).orElse("unPass");
            Assert.notNull(status,"流程参数有误，操作失败");
            RuntimeService runtimeService = getProcessEngine().getRuntimeService();
            runtimeService.updateBusinessStatus(processEvent.getProcessInstanceId(),statusMap.get(status));
        }

        log.info("Flowable事件监听 onEvent name:{}", Optional.ofNullable(event.getType()).map(item -> item.name()).orElse("name 为空"));
    }

    public ProcessEngine getProcessEngine(){
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:mysql://127.0.0.1:3306/flowable-learn2?serverTimezone=UTC&nullCatalogMeansCurrent=true")
                .setJdbcUsername("root")
                .setJdbcPassword("tiger")
                .setJdbcDriver("com.mysql.cj.jdbc.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        return cfg.buildProcessEngine();
    }

    @Override
    public boolean isFailOnException() {
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
