package com.test.service.main

import org.flowable.engine.ProcessEngine
import org.flowable.engine.ProcessEngineConfiguration
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration
import org.flowable.engine.repository.Deployment
import kotlin.test.BeforeTest
import kotlin.test.Test


class FlowableTest {
    private var processEngine: ProcessEngine? = null

    @BeforeTest
    fun beforeTest() {
        // 配置数据库相关信息 获取 ProcessEngineConfiguration
        val cfg = StandaloneProcessEngineConfiguration()
            .setJdbcUrl("jdbc:mysql://127.0.0.1:3306/flowable?serverTimezone=UTC&nullCatalogMeansCurrent=true")
            .setJdbcUsername("root")
            .setJdbcPassword("tiger")
            .setJdbcDriver("com.mysql.cj.jdbc.Driver")
            .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)

        // 获取流程引擎对象
        processEngine = cfg.buildProcessEngine()
    }

    @Test
    fun test001() {
        println("出适化数据库")
    }

    /**
     * 流程部署
     */
    @Test
    open fun testDeploy() {
        // 部署流程 获取RepositoryService对象
        val repositoryService = processEngine!!.repositoryService
        val deployment: Deployment = repositoryService.createDeployment() // 创建Deployment对象
            .addClasspathResource("flowable/2023年出差流程.bpmn20.xml") // 添加流程部署文件
            .name("2023年出差流程") // 设置部署流程的名称
            .deploy() // 执行部署操作
        System.out.println("deployment.getId() = " + deployment.getId())
        System.out.println("deployment.getName() = " + deployment.getName())
        System.out.println("deployment.getKey() = " + deployment.getKey())
    }

    /**
     * 发起流程
     */
    @Test
    fun testStartProcess() {
        //通过RuntimeService来启动流程实例
        val runtimeService = processEngine!!.runtimeService
        //构建流程变量
        val variables: MutableMap<String, Any> = HashMap()
        variables["zuzhang"] = "组长"
        variables["chejian"] = "车间主任"
        variables["caiwu"] = "财务"
        variables["zongjingli"] = "总经理"
        variables["num"] = 10
        val holidayRequest = runtimeService.startProcessInstanceByKey("a2023chuchai", "dagongzai", variables)
        println("流程定义的ID：" + holidayRequest.processDefinitionId)
        println("流程实例的ID：" + holidayRequest.id)
        println("当前活动的ID：" + holidayRequest.activityId)
    }

}