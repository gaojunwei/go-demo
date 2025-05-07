package com.test.service.spring.service

import com.gjw.go.common.log
import com.test.service.spring.AbstractSpringTest
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RuntimeService
import org.junit.jupiter.api.Test
import javax.annotation.Resource


class FlowableServiceTest : AbstractSpringTest() {
    @Resource
    lateinit var processEngine: ProcessEngine

    @Test
    fun startProcess() {
        //通过RuntimeService来启动流程实例
        val runtimeService: RuntimeService = processEngine.runtimeService

        //构建流程变量
        val variables: MutableMap<String, Any> = HashMap()
        variables["dagongzai"] = "打工仔"
        variables["xiangmu"] = "项目经理"
        variables["jishu"] = "技术经理"
        variables["zongjingli"] = "总经理"
        variables["num"] = 10
        val holidayRequest = runtimeService.startProcessInstanceByKey("a2023_ask_for_leave_form", "dagongzai", variables)
        log.info("流程定义的ID：" + holidayRequest.processDefinitionId)
        log.info("流程实例的ID：" + holidayRequest.id)
        log.info("当前活动的ID：" + holidayRequest.activityId)
    }

}