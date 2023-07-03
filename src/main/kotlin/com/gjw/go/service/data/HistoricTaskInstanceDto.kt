package com.gjw.go.service.data

import java.util.*

class HistoricTaskInstanceDto {
    var id: String? = null
    var name: String? = null
    var createTime: Date? = null
    var endTime: Date? = null
    var processInstanceId: String? = null
    var processVariables: Map<String, Any>? = null
    var processDefinitionId: String? = null
    var durationInMillis: Long? = null
    var workTimeInMillis: Long? = null
}