package com.gjw.go.service.impl

import com.gjw.go.service.FlowableService
import com.gjw.go.service.data.ProcessInstanceDTO
import org.flowable.engine.RuntimeService
import org.springframework.stereotype.Component
import javax.annotation.Resource

@Component("flowableService")
class FlowableServiceImpl : FlowableService {
    @Resource
    lateinit var runtimeService: RuntimeService
    override fun listAllProcessInstance(): List<ProcessInstanceDTO> {
        var list = runtimeService.createProcessInstanceQuery().list()
        return list.map {
            ProcessInstanceDTO().apply {
                processInstanceId = it.processInstanceId
                processDefinitionId = it.processDefinitionId
                processDefinitionKey = it.processDefinitionKey
                processDefinitionName = it.processDefinitionName
                processVariables = it.processVariables
                businessKey = it.businessKey
                businessStatus = it.businessStatus
                startTime = it.startTime
                startUserId = it.startUserId
                isEnded = it.isEnded
                isSuspended = it.isSuspended
            }
        }
    }
}