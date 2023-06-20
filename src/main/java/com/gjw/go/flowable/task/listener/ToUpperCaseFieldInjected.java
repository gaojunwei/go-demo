package com.gjw.go.flowable.task.listener;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.common.engine.api.delegate.Expression;

/**
 * java服务组件：指定实现了JavaDelegate或ActivityBehavior的类
 */
@Slf4j
public class ToUpperCaseFieldInjected implements JavaDelegate {
    //字段注入
    private Expression text;

    public void execute(DelegateExecution execution) {
        log.info("java服务组件 text:{}",text.getValue(execution));
        //设置流程变量
        execution.setVariable("jiaoshi", ((String) text.getValue(execution)).toUpperCase());
    }
}
