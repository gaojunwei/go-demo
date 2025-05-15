package com.test.service.spring.service

import com.gjw.go.common.log
import com.test.service.spring.AbstractSpringTest
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RuntimeService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.IOException
import javax.annotation.Resource


/**
 * multiInstanceLoopCharacteristics  测试
 */
class MultiInstanceFlowableServiceTest : AbstractSpringTest() {
    @Resource
    lateinit var processEngine: ProcessEngine

    @BeforeEach
    @DisplayName("部署流程")
    fun testDeploy() {
        // 部署流程 获取RepositoryService对象
        val deployment = processEngine.repositoryService.createDeployment() // 创建Deployment对象
            .addClasspathResource("spring-合同签署.bpmn20.xml") // 添加流程部署文件
            .deploy() // 执行部署操作
        println("deployment.getId() = " + deployment.id)
        println("deployment.getName() = " + deployment.name)
        println("deployment.getKey() = " + deployment.key)
        println("流程部署完成")
    }

    @Test
    @DisplayName("启动流程实例")
    fun startProcess() {
        //通过RuntimeService来启动流程实例
        val runtimeService: RuntimeService = processEngine.runtimeService
        //构建流程变量
        val variables: MutableMap<String, Any> = HashMap()
        variables["dagongzaiList"] = emptyList<String>()
        variables["zongjingli"] = "总经理"
        variables["dagongzaiAudit"] = emptyList<Boolean>()
        val holidayRequest = runtimeService.startProcessInstanceByKey("spring_he_tong", "1001", variables)
        log.info("流程定义的ID：" + holidayRequest.processDefinitionId)
        log.info("流程实例的ID：" + holidayRequest.id)
    }

}