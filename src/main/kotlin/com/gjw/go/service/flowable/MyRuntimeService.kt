package com.gjw.go.service.flowable

import com.gjw.go.service.param.FlowableStartProcessDto
import com.gjw.go.service.data.ProcessInstanceDto

interface MyRuntimeService {
    /**
     * 获取所有进行中流程实例
     */
    fun listAllProcessInstance(): List<ProcessInstanceDto>

    /**
     * 启动流程
     */
    fun startProcess(flowableStartProcess: FlowableStartProcessDto): ProcessInstanceDto
}