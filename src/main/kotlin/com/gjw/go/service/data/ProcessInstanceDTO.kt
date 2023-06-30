package com.gjw.go.service.data

import java.util.*

/**
 * 流程实例信息
 */
class ProcessInstanceDTO {
    var processInstanceId: String? = null
    var processDefinitionId: String? = null
    var processDefinitionKey: String? = null
    var processDefinitionName: String? = null
    var processVariables: Map<String, Any>? = null
    var businessKey: String? = null
    var businessStatus: String? = null
    var startTime: Date? = null
    var startUserId: String? = null
    var isEnded: Boolean? = null
    var isSuspended: Boolean? = null
}
