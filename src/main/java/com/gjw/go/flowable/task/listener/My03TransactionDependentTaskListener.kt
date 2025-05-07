package com.gjw.go.flowable.task.listener

import com.gjw.go.common.log
import org.flowable.bpmn.model.Task
import org.flowable.engine.TaskService
import org.flowable.engine.delegate.TransactionDependentTaskListener
import org.springframework.stereotype.Component

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
        log.info("事务提交后 任务监听器 processId: " + processInstanceId + "，任务ID=" + task!!.id)
        val task = taskService.createTaskQuery().processInstanceId(processInstanceId).executionId(executionId)
            .includeIdentityLinks().singleResult()
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