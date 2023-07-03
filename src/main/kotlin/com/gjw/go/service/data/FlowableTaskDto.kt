package com.gjw.go.service.data

/**
 * 工作流任务实体
 */
class FlowableTaskDto {
    var id: String? = null
    var name: String? = null
    var taskDefinitionId: String? = null
    var taskDefinitionKey: String? = null
    var processInstanceId: String? = null
    var processVariables: Map<String, Any>? = null
    var taskLocalVariables: Map<String, Any>? = null
    var isSuspended: Boolean? = null
}