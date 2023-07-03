package com.gjw.go.service.flowable

import com.alibaba.fastjson2.JSON
import com.gjw.go.AbstractSpringTest
import com.gjw.go.common.inline.log
import com.gjw.go.service.param.FlowableStartProcessDto
import org.flowable.common.engine.impl.identity.Authentication
import javax.annotation.Resource
import kotlin.test.Test

class MyRuntimeServiceTest : AbstractSpringTest() {
    @Resource
    lateinit var myRuntimeService: MyRuntimeService

    //流程变量
    var variableMap = mutableMapOf(
        Pair("zuzhang", "组长"),
        Pair("chejian", "车间主任"),
        Pair("caiwu", "财务"),
        Pair("zongjingli", "总经理"),
        Pair("num", 10)
    )

    //流程定义KEY
    val processDefinitionKeyStr = "a2023chuchai"

    /**
     * 查询所有进行中的流程实例
     */
    @Test
    fun listAllProcessInstance() {
        var list = myRuntimeService.listAllProcessInstance()
        log.info("查询所有进行中的流程实例 {}", JSON.toJSONString(list))
    }

    /**
     * 启动流程
     */
    @Test
    fun startProcess() {
        var startProcessDto = FlowableStartProcessDto().apply {
            processDefinitionKey = processDefinitionKeyStr
            variables = variableMap
            businessKey = "xiaoLiu"
            currentUserId = "gjw"
        }
        var processInstanceDto = myRuntimeService.startProcess(startProcessDto)
        log.info("启动流程 {}", JSON.toJSONString(processInstanceDto))
    }
}