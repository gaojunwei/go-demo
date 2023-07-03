package com.gjw.go.common.inline

import com.gjw.go.service.data.*
import org.flowable.engine.history.HistoricProcessInstance
import org.flowable.engine.repository.ProcessDefinition
import org.flowable.engine.runtime.ProcessInstance
import org.flowable.task.api.Task
import org.flowable.task.api.history.HistoricTaskInstance

/**
 * 流程实例转Dto
 */
inline fun ProcessInstance.toDto(): ProcessInstanceDto {
    return ProcessInstanceDto().also {
        it.processInstanceId = this.processInstanceId
        it.processDefinitionId = this.processDefinitionId
        it.processDefinitionKey = this.processDefinitionKey
        it.processDefinitionName = this.processDefinitionName
        it.processVariables = this.processVariables
        it.businessKey = this.businessKey
        it.businessStatus = this.businessStatus
        it.startTime = this.startTime
        it.startUserId = this.startUserId
        it.isEnded = this.isEnded
        it.isSuspended = this.isSuspended
    }
}

/**
 * 任务实例转Dto
 */
inline fun Task.toDto(): FlowableTaskDto {
    return FlowableTaskDto().also {
        it.id = this.id
        it.name = this.name
        it.processInstanceId = this.processInstanceId
        it.processVariables = this.processVariables
        it.taskDefinitionId = this.taskDefinitionId
        it.taskDefinitionKey = this.taskDefinitionKey
        it.taskLocalVariables = this.taskLocalVariables
        it.isSuspended = this.isSuspended
    }
}

/**
 * 历史流程实例转Dto
 */
inline fun HistoricProcessInstance.toDto(): HistoricProcessInstanceDto {
    return HistoricProcessInstanceDto().also {
        it.id = this.id
        it.name = this.name
        it.startTime = this.startTime
        it.endTime = this.endTime
        it.businessKey = this.businessKey
        it.businessStatus = this.businessStatus
        it.deploymentId = this.deploymentId
        it.description = this.description
        it.durationInMillis = this.durationInMillis
        it.processDefinitionId = this.processDefinitionId
        it.processDefinitionKey = this.processDefinitionKey
        it.processDefinitionName = this.processDefinitionName
        it.processVariables = this.processVariables
        it.startUserId = this.startUserId
    }
}

/**
 * 历史流程实例审批任务转Dto
 */
inline fun HistoricTaskInstance.toDto(): HistoricTaskInstanceDto {
    return HistoricTaskInstanceDto().also {
        it.id = this.id
        it.name = this.name
        it.createTime = this.createTime
        it.endTime = this.endTime
        it.processInstanceId = this.processInstanceId
        it.processVariables = this.processVariables
        it.processDefinitionId = this.processDefinitionId
        it.durationInMillis = this.durationInMillis
        it.workTimeInMillis = this.workTimeInMillis
    }
}

/**
 * 流程定义实例转Dto
 */
inline fun ProcessDefinition.toDto(): ProcessDefinitionDto {
    return ProcessDefinitionDto().also {
        it.id = this.id
        it.name = this.name
        it.deploymentId = this.deploymentId
        it.description = this.description
        it.isSuspended = this.isSuspended
        it.category = this.category
        it.key = this.key
        it.tenantId = this.tenantId
    }
}