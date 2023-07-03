package com.gjw.go.service.param

import java.util.Date

/**
 * 历史实例列表查询
 */
class FlowableHiProcessInstanceQuery : PageQueryDto() {
    var processInstanceId: String? = null
    var processInstanceNameLike: String? = null
    var processDefinitionKey: String? = null
    var startedBefore: Date? = null
    var startedAfter: Date? = null
    var involvedUser: String? = null
}