package com.test.service.spring.service

import com.alibaba.fastjson2.JSON
import com.gjw.go.service.FlowableService
import com.test.service.spring.AbstractSpringTest
import javax.annotation.Resource
import kotlin.test.Test

class FlowableServiceTest : AbstractSpringTest() {

    @Resource
    lateinit var flowableService: FlowableService

    /**
     * 查询所有进行中的流程
     */
    @Test
    fun test001() {
        var list = flowableService.listAllProcessInstance()
        println(JSON.toJSONString(list))
    }
}