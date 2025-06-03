package com.test.service.spring.service

import com.alibaba.fastjson2.JSON
import com.gjw.go.common.log
import com.gjw.go.flowable.dto.DeviceInfo
import com.gjw.go.flowable.dto.DeviceTypeEnums
import com.test.service.spring.AbstractSpringTest
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.repository.ProcessDefinition
import org.flowable.identitylink.api.IdentityLinkType
import org.junit.jupiter.api.DisplayName
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
        // 设备信息
        variables["bigDevices"] =  //emptyList<DeviceInfo>()
        listOf(DeviceInfo().apply {
            this.deviceId = "eqp001"
            this.deviceType = DeviceTypeEnums.BIG
            this.oneDagongzai = listOf("eqp001-dgz01", "eqp001-dgz02")
            this.oneZongjingli = listOf("eqp001-zjl01", "eqp001-zjl02")
        }, DeviceInfo().apply {
            this.deviceId = "eqp002"
            this.deviceType = DeviceTypeEnums.BIG
            this.oneDagongzai = listOf("eqp002-dgz01", "eqp002-dgz02")
            this.oneZongjingli = listOf("eqp002-zjl01", "eqp002-zjl02")
        })

        //variables["smallDevices"] = emptyList<DeviceInfo>()
        variables["smallDevices"] =  //emptyList<DeviceInfo>()
            listOf(DeviceInfo().apply {
            this.deviceId = "eqp003"
            this.deviceType = DeviceTypeEnums.SMALL
            this.twoDagongzaiList = listOf("eqp003-dgz01", "eqp003-dgz02")
        }, DeviceInfo().apply {
            this.deviceId = "eqp004"
            this.deviceType = DeviceTypeEnums.SMALL
            this.twoDagongzaiList = listOf("eqp004-dgz01", "eqp004-dgz02")
        })

        val subTwoProcess: ProcessDefinition = repositoryService.createProcessDefinitionQuery()
            .processDefinitionKey("spring_sub_two")
            .latestVersion()
            .singleResult()
        variables["spring_sub_two_process_id"] = subTwoProcess.id

        val subOneProcess: ProcessDefinition = repositoryService.createProcessDefinitionQuery()
            .processDefinitionKey("spring_sub_one")
            .latestVersion()
            .singleResult()
        variables["spring_sub_one_process_id"] = subOneProcess.id

        //通过RuntimeService来启动流程实例
        val holidayRequest = runtimeService.startProcessInstanceByKey("spring_call_activity", "dagongzai", variables)
        log.info("流程定义的ID：" + holidayRequest.processDefinitionId)
        log.info("流程实例的ID：" + holidayRequest.id)
        log.info("当前活动的ID：" + holidayRequest.activityId)
        log.info("*********************************************")
        processInstanceId = holidayRequest.id
        log.info("*********************************************")

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
        val taskAssignee = "2" // 审批人
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
    fun closeProcess() {
        // 结束流程实例
        runtimeService.deleteProcessInstance("4a2c6d60-401d-11f0-8bca-00ff69377844", "测试结束流程2")
        println("流程实例已结束")
    }

    @Test
    fun updateProcess() {
        val variables: MutableMap<String, Any> = HashMap()
        variables["zongjingli"] = "dsb"
        // 更新流程变量
        runtimeService.setVariables(processInstanceId, variables)
        println("更新流程变量成功")
    }

    private var processInstanceId = "3780c585-4051-11f0-8aad-00ff69377844"

    @Test
    fun processInfo() {
        val process =
            runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).includeProcessVariables()
                .singleResult()
        println("流程定义的ID：${process.processInstanceId}> ${process.superExecutionId}")
        process.processVariables.keys.forEach {
            if (Objects.equals(it, "objInfo")) {
                val objInfo = process.processVariables[it] as ObjInfo
                println("objInfo: ${JSON.toJSONString(objInfo)}")
            }
            println("key: $it = ${process.processVariables[it]}")
        }
        processEngine.taskService.createTaskQuery().processInstanceId(processInstanceId).includeIdentityLinks().list().forEach { task ->
            println("任务ID:*****************")
            println("任务ID: ${task.id}, 任务名称: ${task.name}, 任务状态: ${task.taskDefinitionKey},实例ID：${task.processInstanceId}")
            println("任务ID: ${task.taskDefinitionKey}-》: ${task.taskDefinitionId}")
            println("任务ID: ${task.processDefinitionId}-》: ${task.scopeDefinitionId}")
            println("任务ID: 候选人-》: ${JSON.toJSONString(task.identityLinks.filter { it.type == IdentityLinkType.CANDIDATE }.map { it.userId })}")
            println("任务ID: 受让人-》: ${JSON.toJSONString(task.identityLinks.filter { it.type == IdentityLinkType.ASSIGNEE }.map { it.userId })}")
        }
    }

    @Test
    @DisplayName("流程实例删除")
    fun testDeployDeletedd() {
        val repositoryService = processEngine.repositoryService
        //删除流程，指定流程ID,如果部署的流程启动了就不允许删除了
        //repositoryService.deleteDeployment("2501");
        //第二个参数是级联删除，如果流程启动了 相关的任务一并被删除
        listOf(
            "7483279a-4057-11f0-9674-00ff69377844",
            "74b9ef0e-4057-11f0-9674-00ff69377844",
            "74cd9e22-4057-11f0-9674-00ff69377844",
        ).forEach {
            repositoryService.deleteDeployment( it,true)
        }
        testDeploy()
        startProcess()
        println("开始审核**********")
        doAudit()
    }

    @Test
    @DisplayName("部署流程")
    fun testDeploy() {
        // 部署流程 获取RepositoryService对象
        deploy("spring-call-activity.bpmn20.xml", "spring-call-activity")
        println("**********")
        deploy("spring-sub-two.bpmn20.xml", "spring-sub-two")
        println("**********")
        deploy("spring-sub-one.bpmn20.xml", "spring-sub-one")
        println("**********")
    }

    private fun deploy(resourceName: String, name: String) {
        val deployment = processEngine.repositoryService.createDeployment() // 创建Deployment对象
            .addClasspathResource(resourceName) // 添加流程部署文件
            .name(name) // 设置部署流程的名称
            .deploy() // 执行部署操作

        println("deployment.getId() = " + deployment.id)
        println("deployment.getName() = " + deployment.name)
        println("deployment.getKey() = " + deployment.key)
    }

    @Test
    @DisplayName("流程实例删除")
    fun testDeployDeletedddd() {
        val runtimeService = processEngine.runtimeService
        runtimeService.deleteProcessInstance("abe96fa2-4057-11f0-af32-00ff69377844", "废除")
    }
}