package com.gjw.go.service.flowable

import com.gjw.go.common.result.PageRespDto
import com.gjw.go.service.data.FlowableTaskDto
import com.gjw.go.service.param.FlowableTaskQueryDto
import com.gjw.go.service.param.TaskCompleteParamDto

interface MyTaskService {
    /**
     * 指定人员代办流程
     */
    fun listTodoProcessInstance(flowableTaskQueryDto: FlowableTaskQueryDto): PageRespDto<FlowableTaskDto>

    /**
     * 完成任务审批
     */
    fun completeTask(taskCompleteParamDto: TaskCompleteParamDto)
}