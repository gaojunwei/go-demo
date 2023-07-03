package com.gjw.go.service.param

import java.util.*

class FlowableTaskQueryDto : PageQueryDto() {
    /**
     * 流程定义编码
     */
    var processDefinitionKey: String? = null

    /**
     * 受让人或候选者
     */
    var taskCandidateOrAssigned: String? = null

    /**
     * 任务名称模糊查询
     */
    var taskNameLike: String? = null

    /**
     * 任务开始时间
     */
    var taskCreatedStart: Date? = null

    /**
     * 任务结束时间
     */
    var taskCreatedEnd: Date? = null

    /**
     * 任务发起人
     */
    var taskInvolvedUser:String? = null
}