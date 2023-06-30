package com.gjw.go.service

import com.gjw.go.service.data.ProcessInstanceDTO

interface FlowableService {

    /**
     * 获取所有进行中流程实例
     */
    fun listAllProcessInstance(): List<ProcessInstanceDTO>

}