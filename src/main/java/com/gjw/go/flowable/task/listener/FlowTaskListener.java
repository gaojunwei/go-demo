package com.gjw.go.flowable.task.listener;

import lombok.extern.slf4j.Slf4j;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;

import java.util.*;

/**
 * 任务监听器，通过任务监听器自定义指派
 */
@Slf4j
public class FlowTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("**************任务监听器 start");
        log.info("getEventName= {}",delegateTask.getEventName());
        log.info("getName= {}",delegateTask.getName());
        log.info("getId= {}",delegateTask.getId());
        log.info("getVariables= {}",delegateTask.getVariables());
        log.info("delegateTask.getProcessDefinitionId()= {}",delegateTask.getProcessDefinitionId());
        Map<String,Object> variablesMap = delegateTask.getVariables();
        if(Objects.equals(variablesMap.get("employee"),"寒冬")){
            List<String> users = new ArrayList<>();
            users.add("admin1");
            users.add("admin2");
            users.add("admin3");
            users.add("admin4");
            delegateTask.addCandidateUsers(users);//给该节点添加候选审批用户
            delegateTask.setAssignee("xiangcunlaoshi");//设置受让人
            delegateTask.setDueDate(new Date(System.currentTimeMillis()+60000));
            System.out.println("进入自定义逻辑 ");
        }
        log.info("**************任务监听器 end");
    }
}
