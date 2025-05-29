package com.test.service.spring.service

import com.alibaba.fastjson2.JSON
import com.gjw.go.common.log
import com.test.service.spring.AbstractSpringTest
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.repository.ProcessDefinition
import org.junit.jupiter.api.Test
import java.io.Serializable
import java.util.*
import javax.annotation.Resource


class FlowableServiceTest : AbstractSpringTest() {
    @Resource
    lateinit var processEngine: ProcessEngine

    @Resource
    lateinit var runtimeService: RuntimeService

    @Resource
    lateinit var repositoryService: RepositoryService

    private val processInstanceId = "6043f0d5-3c54-11f0-a7a1-00ff69377844"

    open class ObjInfo : Serializable {
        var id: String? = null
        var name: String? = null
        var age: Int? = null
    }

    @Test
    fun startProcess() {
        val variables: MutableMap<String, Any> = HashMap()
        variables["subFlag"] = false
        variables["objInfo"] = ObjInfo().apply {
            id = "123"
            name = "张三"
            age = 18
        }
        variables["s_dagongzaiList"] = listOf("1", "2")
        // 候选人
        variables["dagongzai"] = "打工仔"

        val subProcess: ProcessDefinition = repositoryService.createProcessDefinitionQuery()
            .processDefinitionKey("spring_sub_two")
            .latestVersion()
            .singleResult()
        variables["spring_sub_two_process_id"] = subProcess.id

        //通过RuntimeService来启动流程实例
        val holidayRequest = runtimeService.startProcessInstanceByKey("spring_call_activity", "dagongzai", variables)
        log.info("流程定义的ID：" + holidayRequest.processDefinitionId)
        log.info("流程实例的ID：" + holidayRequest.id)
        log.info("当前活动的ID：" + holidayRequest.activityId)
    }

    /**
     * 完成审批
     */
    @Test
    fun doAudit() {
        val taskAssignee = "打工仔" // 审批人
        val taskService = processEngine.taskService
        val tasks = taskService.createTaskQuery()
            .taskAssignee(taskAssignee)
            .list()
        // 添加流程变量
        val variables: MutableMap<String, Any> = HashMap()
        variables[taskAssignee + "result"] = "$taskAssignee-通过" // 拒绝请假
        // 完成任务
        for (task in tasks) {
            taskService.complete(task.id, variables)
            println(taskAssignee + " 通过 " + task.id + " " + task.name + " 流程实例ID：" + task.processInstanceId)
        }
    }

    /**
     * 完成审批
     */
    @Test
    fun doAudit2() {
        val taskAssignee = "1" // 审批人
        val taskService = processEngine.taskService
        val tasks = taskService.createTaskQuery()
            .taskAssignee(taskAssignee)
            .list()
        // 添加流程变量
        val variables: MutableMap<String, Any> = HashMap()
        variables[taskAssignee + "result"] = "$taskAssignee-通过" // 拒绝请假
        // 完成任务
        for (task in tasks) {
            taskService.complete(task.id, variables)
            println(taskAssignee + " 通过 " + task.id + " " + task.name + " 流程实例ID：" + task.processInstanceId)
        }
    }

    @Test
    fun updateProcess() {
        val variables: MutableMap<String, Any> = HashMap()
        variables["zongjingli"] = "dsb"
        // 更新流程变量
        runtimeService.setVariables(processInstanceId, variables)
        println("更新流程变量成功")
    }

    @Test
    fun processInfo() {
        val process =
            runtimeService.createProcessInstanceQuery().superProcessInstanceId(processInstanceId).includeProcessVariables()
                .singleResult()
        println("流程定义的ID：${process.processInstanceId}> ${process.parentId}")
        process.processVariables.keys.forEach {
            if (Objects.equals(it, "objInfo")) {
                val objInfo = process.processVariables[it] as ObjInfo
                println("objInfo: ${JSON.toJSONString(objInfo)}")
            }
            println("key: $it = ${process.processVariables[it]}")
        }
        processEngine.taskService.createTaskQuery().processInstanceId(processInstanceId).list().forEach { task ->
            println("任务ID: ${task.id}, 任务名称: ${task.name}, 任务状态: ${task.taskDefinitionKey}")
            println("任务ID: ${task.taskDefinitionKey}-》: ${task.taskDefinitionId}")
            println("任务ID: ${task.processDefinitionId}-》: ${task.scopeDefinitionId}")
        }
    }
}