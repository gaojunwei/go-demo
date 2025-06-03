package com.gjw.go.flowable.task.listener

import com.alibaba.fastjson2.JSON
import com.gjw.go.common.log
import org.flowable.engine.TaskService
import org.flowable.identitylink.api.IdentityLink
import org.flowable.task.service.delegate.DelegateTask
import org.flowable.task.service.delegate.TaskListener
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class My03TaskListener(
    private val taskService: TaskService
) : TaskListener {

    override fun notify(delegateTask: DelegateTask) {
        val list = taskService.getIdentityLinksForTask(delegateTask.id).stream().map { identityLink: IdentityLink -> identityLink.userId }.collect(Collectors.toList())
        log.info("事务中执行的 任务监听器 processId: " + delegateTask.processInstanceId + "，任务ID=" + delegateTask.id + "，任务候选人列表：" + JSON.toJSONString(list))
    }
}