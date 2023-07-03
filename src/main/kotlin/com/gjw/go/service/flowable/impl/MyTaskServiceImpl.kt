package com.gjw.go.service.flowable.impl

import com.gjw.go.common.result.PageRespDto
import com.gjw.go.service.data.FlowableTaskDto
import com.gjw.go.service.flowable.MyTaskService
import com.gjw.go.service.param.FlowableTaskQueryDto
import com.gjw.go.service.param.TaskCompleteParamDto
import org.flowable.engine.TaskService
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.annotation.Resource

@Component("myTaskService")
class MyTaskServiceImpl : MyTaskService {
    @Resource
    lateinit var taskService: TaskService
    override fun listTodoProcessInstance(flowableTaskQueryDto: FlowableTaskQueryDto): PageRespDto<FlowableTaskDto> {
        var taskQuery = taskService.createTaskQuery().includeProcessVariables().includeTaskLocalVariables()
            .orderByTaskCreateTime().desc()
        flowableTaskQueryDto.processDefinitionKey.let { taskQuery.processDefinitionKey(it) }
        flowableTaskQueryDto.taskCandidateOrAssigned.let { taskQuery.taskCandidateOrAssigned(it) }
        flowableTaskQueryDto.taskCreatedStart.let { taskQuery.taskCreatedAfter(it) }
        flowableTaskQueryDto.taskCreatedEnd.let { taskQuery.taskCreatedBefore(it) }
        flowableTaskQueryDto.taskNameLike?.let { taskQuery.taskNameLike(it) }
        flowableTaskQueryDto.taskInvolvedUser?.let { taskQuery.taskInvolvedUser(it) }

        var list = taskQuery.listPage(flowableTaskQueryDto.getStarIndex(), flowableTaskQueryDto.pageSize)
        var count = taskQuery.count()
        return PageRespDto.success(flowableTaskQueryDto.pageSize, flowableTaskQueryDto.pageNo, count, list.map {
            FlowableTaskDto().apply {
                this.id = it.id
                name = it.name
                processInstanceId = it.processInstanceId
                processVariables = it.processVariables
                taskDefinitionId = it.taskDefinitionId
                taskDefinitionKey = it.taskDefinitionKey
                taskLocalVariables = it.taskLocalVariables
                isSuspended = it.isSuspended
            }
        })
    }

    override fun completeTask(@Validated taskCompleteParamDto: TaskCompleteParamDto) {
        taskService.complete(taskCompleteParamDto.taskId, taskCompleteParamDto.variables)
    }
}