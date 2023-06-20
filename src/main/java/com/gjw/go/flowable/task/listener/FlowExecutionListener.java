package com.gjw.go.flowable.task.listener;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

@Slf4j
@Setter
@Getter
public class FlowExecutionListener implements ExecutionListener {
    private Expression fieldName;
    @Override
    public void notify(DelegateExecution execution) {
        log.info("********** 执行 监听器 start");
        log.info("fieldName.getExpressionText() = {}",fieldName.getExpressionText());
        log.info("fieldName.getValue = {}",fieldName.getValue(execution));
        log.info("getEventName = {}",execution.getEventName());
        log.info("getProcessInstanceId = {}",execution.getProcessInstanceId());
        log.info("getProcessDefinitionId = {}",execution.getProcessDefinitionId());
        log.info("getVariables = {}",execution.getVariables());
        log.info("********** 执行 监听器 end");
    }
}
