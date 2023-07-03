package com.gjw.go.service.data

import java.util.*

class HistoricProcessInstanceDto {
    var id: String? = null
    var name: String? = null
    var startTime: Date? = null
    var endTime: Date? = null
    var businessKey: String? = null
    var businessStatus: String? = null
    var deploymentId: String? = null
    var description: String? = null
    var durationInMillis: Long? = null
    var processDefinitionId: String? = null
    var processDefinitionKey: String? = null
    var processDefinitionName: String? = null
    var processVariables: Map<String, Any>? = null
    var startUserId: String? = null
}