package com.gjw.go.flowable.task.listener

import com.alibaba.fastjson2.JSON
import com.gjw.go.common.log
import org.flowable.bpmn.model.Task
import org.flowable.engine.TaskService
import org.flowable.engine.delegate.TransactionDependentTaskListener
import org.flowable.identitylink.api.IdentityLink
import org.flowable.identitylink.api.IdentityLinkType
import org.springframework.stereotype.Component
import java.util.stream.Collectors

/**
 * 事务提交后 执行的任务监听器
 */
@Component
class My03TransactionCommittedTaskListener(
    private val taskService: TaskService
) : TransactionDependentTaskListener {
    override fun notify(
        processInstanceId: String?,
        executionId: String?,
        task: Task?,
        executionVariables: MutableMap<String, Any>?,
        customPropertiesMap: MutableMap<String, Any>?
    ) {
        val task = taskService.createTaskQuery().processInstanceId(processInstanceId).executionId(executionId)
            .includeIdentityLinks().singleResult()
        val list = taskService.getIdentityLinksForTask(task.id).stream().filter { it.type == IdentityLinkType.CANDIDATE || it.type == IdentityLinkType.ASSIGNEE }.map { identityLink: IdentityLink -> identityLink.userId }.collect(
            Collectors.toList())
        log.info("事务提交后 任务监听器 processId: " + processInstanceId + "，任务ID=" + task!!.id + "，任务候选人列表：${JSON.toJSONString(list)}")
    }
}
/**
 * 事务回滚后 执行的任务监听器
 */
@Component
class My03TransactionRolledBackTaskListener(
    private val taskService: TaskService
) : TransactionDependentTaskListener {
    override fun notify(
        processInstanceId: String,
        executionId: String,
        task: Task?,
        executionVariables: MutableMap<String, Any>?,
        customPropertiesMap: MutableMap<String, Any>?
    ) {
        log.info("事务回滚 任务监听器 processId: $processInstanceId,executionId: $executionId")
    }
}