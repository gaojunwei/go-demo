package com.gjw.go.service.flowable.impl

import com.gjw.go.common.inline.log
import com.gjw.go.common.inline.toDto
import com.gjw.go.service.data.ProcessInstanceDto
import com.gjw.go.service.flowable.MyRuntimeService
import com.gjw.go.service.param.FlowableStartProcessDto
import org.flowable.common.engine.impl.identity.Authentication
import org.flowable.engine.RuntimeService
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.Resource

@Component("myRuntimeService")
class MyRuntimeServiceImpl : MyRuntimeService {
    @Resource
    lateinit var runtimeService: RuntimeService
    override fun listAllProcessInstance(): List<ProcessInstanceDto> {
        var list = runtimeService.createProcessInstanceQuery().includeProcessVariables().orderByStartTime().desc().list()
        return list.map {
            it.toDto()
        }
    }

    override fun startProcess(flowableStartProcess: FlowableStartProcessDto): ProcessInstanceDto {
        flowableStartProcess.currentUserId.apply { Authentication.setAuthenticatedUserId(this) }
        var processInstance = runtimeService.startProcessInstanceByKey(
            flowableStartProcess.processDefinitionKey,
            flowableStartProcess.businessKey,
            flowableStartProcess.variables
        )
        return processInstance.toDto()
    }
}